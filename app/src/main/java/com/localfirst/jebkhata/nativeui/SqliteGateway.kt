package com.localfirst.jebkhata.nativeui

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID

class SqliteGateway(private val context: Context) : ExpenseGateway {
    private val helper = AppDbHelper(context)

    override fun addQuickEntry(input: String): ExpenseEntry {
        val (amountMinor, note) = parseQuickEntry(input)
        val createdAt = Instant.now().toString()
        val entry = ExpenseEntry(
            id = UUID.randomUUID().toString(),
            amountMinor = amountMinor,
            note = note,
            category = "Food",
            createdAt = createdAt,
        )
        helper.writableDatabase.insert("entries", null, ContentValues().apply {
            put("id", entry.id)
            put("amount_minor", entry.amountMinor)
            put("note", entry.note)
            put("category", entry.category)
            put("created_at", entry.createdAt)
        })
        return entry
    }

    override fun updateEntryQuick(id: String, input: String) {
        val (amountMinor, note) = parseQuickEntry(input)
        helper.writableDatabase.update(
            "entries",
            ContentValues().apply {
                put("amount_minor", amountMinor)
                put("note", note)
                put("category", "Food")
            },
            "id=?",
            arrayOf(id),
        )
    }

    override fun deleteEntry(id: String) {
        helper.writableDatabase.delete("entries", "id=?", arrayOf(id))
    }

    override fun recentEntries(limit: UInt): List<ExpenseEntry> {
        return queryEntries("", limit)
    }

    override fun todayTotalMinor(): Long {
        val db = helper.readableDatabase
        val datePrefix = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC).format(Instant.now())
        db.rawQuery(
            "SELECT COALESCE(SUM(amount_minor),0) FROM entries WHERE substr(created_at,1,10)=?",
            arrayOf(datePrefix),
        ).use { c ->
            return if (c.moveToFirst()) c.getLong(0) else 0L
        }
    }

    override fun allEntries(query: String, limit: UInt): List<ExpenseEntry> = queryEntries(query, limit)

    override fun monthTotalMinor(): Long {
        val db = helper.readableDatabase
        val monthPrefix = DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneOffset.UTC).format(Instant.now())
        db.rawQuery(
            "SELECT COALESCE(SUM(amount_minor),0) FROM entries WHERE substr(created_at,1,7)=?",
            arrayOf(monthPrefix),
        ).use { c ->
            return if (c.moveToFirst()) c.getLong(0) else 0L
        }
    }

    override fun upsertBudget(label: String, limitMinor: Long) {
        val db = helper.writableDatabase
        val existing = db.rawQuery("SELECT id FROM budgets WHERE lower(label)=lower(?)", arrayOf(label)).use { c ->
            if (c.moveToFirst()) c.getLong(0) else null
        }
        val values = ContentValues().apply {
            put("label", label)
            put("limit_minor", limitMinor)
        }
        if (existing == null) db.insert("budgets", null, values) else db.update("budgets", values, "id=?", arrayOf(existing.toString()))
    }

    override fun budgets(): List<BudgetProgress> {
        val db = helper.readableDatabase
        val rows = mutableListOf<BudgetProgress>()
        db.rawQuery("SELECT id, label, limit_minor FROM budgets ORDER BY id DESC", emptyArray()).use { c ->
            while (c.moveToNext()) {
                val id = c.getLong(0)
                val label = c.getString(1)
                val limitMinor = c.getLong(2)
                val spent = if (label.equals("overall", true)) {
                    monthTotalMinor()
                } else {
                    db.rawQuery(
                        "SELECT COALESCE(SUM(amount_minor),0) FROM entries WHERE lower(category)=lower(?)",
                        arrayOf(label),
                    ).use { sumC -> if (sumC.moveToFirst()) sumC.getLong(0) else 0L }
                }
                rows.add(BudgetProgress(id = id, label = label, limitMinor = limitMinor, spentMinor = spent))
            }
        }
        return rows
    }

    override fun exportJson(): String {
        val entries = allEntries("", 1000u)
        val root = JSONObject()
        val arr = JSONArray()
        entries.forEach { e ->
            arr.put(JSONObject().apply {
                put("id", e.id)
                put("amount_minor", e.amountMinor)
                put("note", e.note)
                put("category", e.category)
                put("created_at", e.createdAt)
            })
        }
        root.put("currency", currencyCode())
        root.put("theme_palette", themePaletteKey())
        root.put("entries", arr)

        val dir = File(context.filesDir, "exports")
        dir.mkdirs()
        val file = File(dir, "transactions.json")
        file.writeText(root.toString(2))
        return file.absolutePath
    }

    override fun importJson(): Int {
        val file = File(context.filesDir, "exports/transactions.json")
        if (!file.exists()) return 0
        val root = JSONObject(file.readText())
        val arr = root.optJSONArray("entries") ?: JSONArray()
        val db = helper.writableDatabase
        var imported = 0
        db.beginTransaction()
        try {
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val id = obj.optString("id").ifBlank { UUID.randomUUID().toString() }
                val amountMinor = obj.optLong("amount_minor", 0L)
                val note = obj.optString("note").ifBlank { "entry" }
                val category = obj.optString("category").ifBlank { "Food" }
                val createdAt = obj.optString("created_at").ifBlank { Instant.now().toString() }
                db.insertWithOnConflict(
                    "entries",
                    null,
                    ContentValues().apply {
                        put("id", id)
                        put("amount_minor", amountMinor)
                        put("note", note)
                        put("category", category)
                        put("created_at", createdAt)
                    },
                    SQLiteDatabase.CONFLICT_REPLACE,
                )
                imported += 1
            }
            val currency = root.optString("currency").uppercase()
            if (currency.length == 3) {
                db.execSQL(
                    "INSERT INTO settings(key,value) VALUES('currency',?) ON CONFLICT(key) DO UPDATE SET value=excluded.value",
                    arrayOf(currency),
                )
            }
            val themeKey = root.optString("theme_palette").trim().lowercase()
            if (themeKey.isNotEmpty()) {
                db.execSQL(
                    "INSERT INTO settings(key,value) VALUES('theme_palette',?) ON CONFLICT(key) DO UPDATE SET value=excluded.value",
                    arrayOf(themeKey),
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        return imported
    }

    override fun clearAllData() {
        val db = helper.writableDatabase
        db.beginTransaction()
        try {
            db.delete("entries", null, null)
            db.delete("budgets", null, null)
            db.execSQL(
                "INSERT INTO settings(key,value) VALUES('currency','INR') ON CONFLICT(key) DO UPDATE SET value=excluded.value"
            )
            db.execSQL(
                "INSERT INTO settings(key,value) VALUES('theme_palette','midnight') ON CONFLICT(key) DO UPDATE SET value=excluded.value"
            )
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun currencyCode(): String {
        val db = helper.readableDatabase
        db.rawQuery("SELECT value FROM settings WHERE key='currency' LIMIT 1", emptyArray()).use { c ->
            return if (c.moveToFirst()) c.getString(0) else "INR"
        }
    }

    override fun setCurrencyCode(code: String) {
        val cleaned = code.trim().uppercase()
        if (cleaned.length != 3) return
        val db = helper.writableDatabase
        db.execSQL(
            "INSERT INTO settings(key,value) VALUES('currency',?) ON CONFLICT(key) DO UPDATE SET value=excluded.value",
            arrayOf(cleaned),
        )
    }

    override fun themePaletteKey(): String {
        val db = helper.readableDatabase
        db.rawQuery("SELECT value FROM settings WHERE key='theme_palette' LIMIT 1", emptyArray()).use { c ->
            return if (c.moveToFirst()) c.getString(0) else "midnight"
        }
    }

    override fun setThemePaletteKey(key: String) {
        val cleaned = key.trim().lowercase()
        if (cleaned.isEmpty()) return
        val db = helper.writableDatabase
        db.execSQL(
            "INSERT INTO settings(key,value) VALUES('theme_palette',?) ON CONFLICT(key) DO UPDATE SET value=excluded.value",
            arrayOf(cleaned),
        )
    }

    override fun diagnostics(): List<Pair<String, String>> {
        val dbFile = context.getDatabasePath(AppDbHelper.DB_NAME)
        val count = helper.readableDatabase.rawQuery("SELECT COUNT(*) FROM entries", emptyArray()).use { c ->
            if (c.moveToFirst()) c.getInt(0) else 0
        }
        return listOf(
            "DB Path" to dbFile.absolutePath,
            "DB Size" to "${dbFile.length() / 1024} KB",
            "Entries" to count.toString(),
            "Currency" to currencyCode(),
            "Theme" to themePaletteKey(),
        )
    }

    private fun queryEntries(query: String, limit: UInt): List<ExpenseEntry> {
        val db = helper.readableDatabase
        val rows = mutableListOf<ExpenseEntry>()
        val max = limit.toInt().coerceIn(1, 500)
        val q = query.trim()
        val sql = if (q.isEmpty()) {
            "SELECT id, amount_minor, note, category, created_at FROM entries ORDER BY created_at DESC LIMIT ?"
        } else {
            "SELECT id, amount_minor, note, category, created_at FROM entries WHERE lower(note) LIKE lower(?) OR lower(category) LIKE lower(?) ORDER BY created_at DESC LIMIT ?"
        }
        val args = if (q.isEmpty()) {
            arrayOf(max.toString())
        } else {
            arrayOf("%$q%", "%$q%", max.toString())
        }
        db.rawQuery(sql, args).use { c ->
            while (c.moveToNext()) {
                rows.add(
                    ExpenseEntry(
                        id = c.getString(0),
                        amountMinor = c.getLong(1),
                        note = c.getString(2),
                        category = c.getString(3),
                        createdAt = c.getString(4),
                    )
                )
            }
        }
        return rows
    }

    private fun parseQuickEntry(raw: String): Pair<Long, String> {
        val parts = raw.trim().split(Regex("\\s+"))
        require(parts.size >= 2) { "Use format: 10 chai" }
        val amountMajor = parts.first().toDoubleOrNull() ?: throw IllegalArgumentException("Amount must be numeric")
        require(amountMajor > 0.0) { "Amount must be > 0" }
        val note = QuickAliasDictionary.expandNote(parts.drop(1).joinToString(" ").trim())
        require(note.isNotEmpty()) { "Missing note" }
        val amountMinor = (amountMajor * 100.0).toLong()
        return amountMinor to note
    }
}

private class AppDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE entries(
                id TEXT PRIMARY KEY,
                amount_minor INTEGER NOT NULL,
                note TEXT NOT NULL,
                category TEXT NOT NULL,
                created_at TEXT NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX idx_entries_created_at ON entries(created_at DESC)")
        db.execSQL(
            """
            CREATE TABLE budgets(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                label TEXT NOT NULL UNIQUE,
                limit_minor INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE settings(
                key TEXT PRIMARY KEY,
                value TEXT NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL("INSERT INTO settings(key,value) VALUES('currency','INR')")
        db.execSQL("INSERT INTO settings(key,value) VALUES('theme_palette','midnight')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit

    companion object {
        const val DB_NAME = "expense_native.db"
        const val DB_VERSION = 1
    }
}

