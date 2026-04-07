package com.localfirst.jebkhata.nativeui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.localfirst.jebkhata.nativeui.ui.ExpenseApp
import com.localfirst.jebkhata.nativeui.ui.theme.ExpenseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        setContent {
            ExpenseTheme {
                ExpenseApp(gateway = SqliteGateway(this))
            }
        }
    }
}

