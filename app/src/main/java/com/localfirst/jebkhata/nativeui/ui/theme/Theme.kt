package com.localfirst.jebkhata.nativeui.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFD8DADF),
    background = Color(0xFF131329),
    surface = Color(0xFF1A1A31),
    onPrimary = Color(0xFF131329),
    onBackground = Color(0xFFD8DADF),
    onSurface = Color(0xFFD8DADF),
)

@Composable
fun ExpenseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkScheme,
        typography = Typography,
        content = content,
    )
}

