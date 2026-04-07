package com.localfirst.jebkhata.nativeui

data class ExpenseEntry(
    val id: String,
    val amountMinor: Long,
    val note: String,
    val category: String,
    val createdAt: String,
)

data class BudgetProgress(
    val id: Long,
    val label: String,
    val limitMinor: Long,
    val spentMinor: Long,
)

interface ExpenseGateway {
    fun addQuickEntry(input: String): ExpenseEntry
    fun updateEntryQuick(id: String, input: String)
    fun deleteEntry(id: String)
    fun recentEntries(limit: UInt): List<ExpenseEntry>
    fun todayTotalMinor(): Long
    fun allEntries(query: String, limit: UInt): List<ExpenseEntry>
    fun monthTotalMinor(): Long
    fun upsertBudget(label: String, limitMinor: Long)
    fun budgets(): List<BudgetProgress>
    fun exportJson(): String
    fun importJson(): Int
    fun clearAllData()
    fun currencyCode(): String
    fun setCurrencyCode(code: String)
    fun themePaletteKey(): String
    fun setThemePaletteKey(key: String)
    fun diagnostics(): List<Pair<String, String>>
}

