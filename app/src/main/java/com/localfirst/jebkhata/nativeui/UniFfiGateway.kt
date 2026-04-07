package com.localfirst.jebkhata.nativeui

/**
 * Replace InMemoryGateway with a UniFFI-backed implementation after generating bindings.
 *
 * Expected binding usage (after generation):
 *   val core = uniffi.expense_core.ExpenseCore(dbPath)
 */
class UniFfiGateway(private val dbPath: String) : ExpenseGateway {
    override fun addQuickEntry(input: String): ExpenseEntry {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun updateEntryQuick(id: String, input: String) {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun deleteEntry(id: String) {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun recentEntries(limit: UInt): List<ExpenseEntry> {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun todayTotalMinor(): Long {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun allEntries(query: String, limit: UInt): List<ExpenseEntry> {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun monthTotalMinor(): Long {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun upsertBudget(label: String, limitMinor: Long) {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun budgets(): List<BudgetProgress> {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun exportJson(): String {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun importJson(): Int {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun clearAllData() {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun currencyCode(): String {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun setCurrencyCode(code: String) {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun themePaletteKey(): String {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun setThemePaletteKey(key: String) {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }

    override fun diagnostics(): List<Pair<String, String>> {
        throw NotImplementedError("Wire to generated UniFFI bindings")
    }
}

