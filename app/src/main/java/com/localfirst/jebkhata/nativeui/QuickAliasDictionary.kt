package com.localfirst.jebkhata.nativeui

object QuickAliasDictionary {
    // Add more entries here: "short" to "expanded".
    val entries: LinkedHashMap<String, String> = linkedMapOf(
        "cig" to "cigarette",
        "cigs" to "cigarettes",
        "tea" to "chai",
        "cof" to "coffee",
        "snk" to "snack",
        "brk" to "breakfast",
        "lnch" to "lunch",
        "dnr" to "dinner",
        "veg" to "vegetables",
        "med" to "medicine",
    )

    fun expandToken(token: String): String {
        val key = token.lowercase()
        return entries[key] ?: token
    }

    fun expandNote(note: String): String {
        return note
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .joinToString(" ") { token -> expandToken(token) }
    }

    fun expandQuickInput(input: String): String {
        val parts = input.trim().split(Regex("\\s+")).toMutableList()
        if (parts.size < 2) return input
        for (i in 1 until parts.size) {
            parts[i] = expandToken(parts[i])
        }
        return parts.joinToString(" ")
    }

    fun firstHint(token: String): String? {
        val q = token.lowercase().trim()
        if (q.isBlank()) return null
        val match = entries.entries.firstOrNull { (short, full) ->
            short.startsWith(q) || full.startsWith(q)
        } ?: return null
        return match.value
    }
}

