package com.localfirst.jebkhata.nativeui

import java.time.Instant
import java.util.UUID

class InMemoryGateway : ExpenseGateway {
    private val entries = mutableListOf<ExpenseEntry>()
    private val budgets = mutableListOf<BudgetProgress>()
    private var currency = "INR"
    private var paletteKey = "midnight"

    override fun addQuickEntry(input: String): ExpenseEntry {
        val parsed = parseQuickEntry(input)
        val entry = ExpenseEntry(
            id = UUID.randomUUID().toString(),
            amountMinor = parsed.first,
            note = parsed.second,
            category = "Food",
            createdAt = Instant.now().toString(),
        )
        entries.add(0, entry)
        return entry
    }

    override fun updateEntryQuick(id: String, input: String) {
        val idx = entries.indexOfFirst { it.id == id }
        require(idx >= 0) { "Entry not found" }
        val parsed = parseQuickEntry(input)
        val existing = entries[idx]
        entries[idx] = existing.copy(
            amountMinor = parsed.first,
            note = parsed.second,
            category = "Food",
        )
    }

    override fun deleteEntry(id: String) {
        entries.removeAll { it.id == id }
    }

    override fun recentEntries(limit: UInt): List<ExpenseEntry> {
        val size = if (limit.toInt() <= 0) 20 else limit.toInt().coerceAtMost(200)
        return entries.take(size)
    }

    override fun todayTotalMinor(): Long = entries.sumOf { it.amountMinor }

    override fun allEntries(query: String, limit: UInt): List<ExpenseEntry> {
        val q = query.trim().lowercase()
        val filtered = if (q.isEmpty()) entries else entries.filter { it.note.lowercase().contains(q) || it.category.lowercase().contains(q) }
        return filtered.take(limit.toInt().coerceIn(1, 500))
    }

    override fun monthTotalMinor(): Long = entries.sumOf { it.amountMinor }

    override fun upsertBudget(label: String, limitMinor: Long) {
        val existing = budgets.indexOfFirst { it.label.equals(label, ignoreCase = true) }
        val spent = entries.filter { it.category.equals(label, ignoreCase = true) || label.equals("overall", ignoreCase = true) }.sumOf { it.amountMinor }
        val row = BudgetProgress(
            id = if (existing >= 0) budgets[existing].id else (budgets.size + 1).toLong(),
            label = label,
            limitMinor = limitMinor,
            spentMinor = spent,
        )
        if (existing >= 0) budgets[existing] = row else budgets.add(0, row)
    }

    override fun budgets(): List<BudgetProgress> = budgets.toList()

    override fun exportJson(): String {
        return """{"entries":${entries.size},"currency":"$currency"}"""
    }

    override fun importJson(): Int {
        return 0
    }

    override fun clearAllData() {
        entries.clear()
        budgets.clear()
    }

    override fun currencyCode(): String = currency

    override fun setCurrencyCode(code: String) {
        val cleaned = code.trim().uppercase()
        if (cleaned.length == 3) currency = cleaned
    }

    override fun themePaletteKey(): String = paletteKey

    override fun setThemePaletteKey(key: String) {
        val cleaned = key.trim().lowercase()
        if (cleaned.isNotEmpty()) paletteKey = cleaned
    }

    override fun diagnostics(): List<Pair<String, String>> {
        return listOf(
            "Entries" to entries.size.toString(),
            "Budgets" to budgets.size.toString(),
            "Currency" to currency,
            "Theme" to paletteKey,
        )
    }

    private fun parseQuickEntry(raw: String): Pair<Long, String> {
        val parts = raw.trim().split(Regex("\\s+"))
        require(parts.size >= 2) { "Use format: 10 chai" }

        val amountMajor = parts.first().toDoubleOrNull()
            ?: throw IllegalArgumentException("Amount must be numeric")
        require(amountMajor > 0.0) { "Amount must be > 0" }

        val note = QuickAliasDictionary.expandNote(parts.drop(1).joinToString(" ").trim())
        require(note.isNotEmpty()) { "Missing note" }

        val amountMinor = (amountMajor * 100.0).toLong()
        return amountMinor to note
    }
}

