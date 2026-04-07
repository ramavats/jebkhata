package com.localfirst.jebkhata.nativeui.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.platform.LocalDensity
import com.localfirst.jebkhata.nativeui.BudgetProgress
import com.localfirst.jebkhata.nativeui.ExpenseEntry
import com.localfirst.jebkhata.nativeui.ExpenseGateway
import com.localfirst.jebkhata.nativeui.QuickAliasDictionary
import com.localfirst.jebkhata.nativeui.R
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle as DateTextStyle
import java.util.Locale
import java.io.File
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.BasicTextField
import kotlin.math.abs
import kotlin.math.roundToInt
import androidx.compose.ui.platform.LocalContext

private enum class Tab { KHATAS, CALENDAR, SHARE, SETTINGS }

private data class AppPalette(
    val key: String,
    val name: String,
    val appBgTop: Color,
    val appBgBottom: Color,
    val primaryText: Color,
    val mutedText: Color,
    val dotColor: Color,
    val navTintActive: Color,
    val navTintInactive: Color,
    val navDivider: Color,
    val calendarDayBox: Color,
    val calendarDaySelected: Color,
    val settingsTitleShadow: Color,
    val danger: Color,
    val aliasHintBg: Color,
    val aliasHintBorder: Color,
    val aliasHintText: Color,
)

private object ThemePalettes {
    const val defaultKey = "midnight"

    // Add more palettes here.
    val all = listOf(
        AppPalette(
            key = "midnight",
            name = "Midnight",
            appBgTop = Color(0xFF1A1A36),
            appBgBottom = Color(0xFF101023),
            primaryText = Color(0xFFD8DADF),
            mutedText = Color(0xFF666B7F),
            dotColor = Color(0xFF0CE3A0),
            navTintActive = Color(0xFF9AA0B6),
            navTintInactive = Color(0xFF6F7388),
            navDivider = Color(0xFF2A2D43),
            calendarDayBox = Color(0xFFE4E4E7),
            calendarDaySelected = Color(0xFF8EA6D1),
            settingsTitleShadow = Color(0xFF323650),
            danger = Color(0xFFFF3B1C),
            aliasHintBg = Color(0x1F8EA6D1),
            aliasHintBorder = Color(0x558EA6D1),
            aliasHintText = Color(0xFFC6D2EA),
        ),
        AppPalette(
            key = "graphite",
            name = "Graphite",
            appBgTop = Color(0xFF1A2028),
            appBgBottom = Color(0xFF0F141A),
            primaryText = Color(0xFFD9DEE5),
            mutedText = Color(0xFF738091),
            dotColor = Color(0xFF5DE2C4),
            navTintActive = Color(0xFFB2BCCA),
            navTintInactive = Color(0xFF788295),
            navDivider = Color(0xFF2A323D),
            calendarDayBox = Color(0xFFE8EBEF),
            calendarDaySelected = Color(0xFF6F8FB8),
            settingsTitleShadow = Color(0xFF3B4454),
            danger = Color(0xFFFF5E4D),
            aliasHintBg = Color(0x1F6F8FB8),
            aliasHintBorder = Color(0x557AA0CA),
            aliasHintText = Color(0xFFC9D8EB),
        ),
        AppPalette(
            key = "ocean",
            name = "Ocean",
            appBgTop = Color(0xFF12253A),
            appBgBottom = Color(0xFF0D1726),
            primaryText = Color(0xFFD9E2EF),
            mutedText = Color(0xFF7287A0),
            dotColor = Color(0xFF2DE4D1),
            navTintActive = Color(0xFFA7BDD7),
            navTintInactive = Color(0xFF6E84A0),
            navDivider = Color(0xFF25364D),
            calendarDayBox = Color(0xFFE5ECF6),
            calendarDaySelected = Color(0xFF5F8FC8),
            settingsTitleShadow = Color(0xFF2F4868),
            danger = Color(0xFFFF6B5A),
            aliasHintBg = Color(0x1F5F8FC8),
            aliasHintBorder = Color(0x557AA5D6),
            aliasHintText = Color(0xFFC8D9EE),
        ),
        AppPalette(
            key = "forest",
            name = "Forest",
            appBgTop = Color(0xFF132521),
            appBgBottom = Color(0xFF0D1715),
            primaryText = Color(0xFFDBE5DE),
            mutedText = Color(0xFF71867A),
            dotColor = Color(0xFF2FE0A2),
            navTintActive = Color(0xFFAFBFB5),
            navTintInactive = Color(0xFF72857A),
            navDivider = Color(0xFF26372F),
            calendarDayBox = Color(0xFFE8EFEA),
            calendarDaySelected = Color(0xFF78A88E),
            settingsTitleShadow = Color(0xFF365244),
            danger = Color(0xFFFF6255),
            aliasHintBg = Color(0x1F6E9E87),
            aliasHintBorder = Color(0x5588B99F),
            aliasHintText = Color(0xFFCFE2D6),
        ),
        AppPalette(
            key = "after_dark_80s",
            name = "80s After Dark",
            appBgTop = Color(0xFF21183B),
            appBgBottom = Color(0xFF151129),
            primaryText = Color(0xFFE6DEF7),
            mutedText = Color(0xFF8E80B0),
            dotColor = Color(0xFFFF6FAE),
            navTintActive = Color(0xFFCBB7F6),
            navTintInactive = Color(0xFF8A7CA8),
            navDivider = Color(0xFF3A3056),
            calendarDayBox = Color(0xFFF1ECFB),
            calendarDaySelected = Color(0xFF9A7CE4),
            settingsTitleShadow = Color(0xFF4A3A73),
            danger = Color(0xFFFF6D8F),
            aliasHintBg = Color(0x2A9A7CE4),
            aliasHintBorder = Color(0x669A7CE4),
            aliasHintText = Color(0xFFE4DAFA),
        ),
        AppPalette(
            key = "aether",
            name = "Aether",
            appBgTop = Color(0xFF112633),
            appBgBottom = Color(0xFF0B1822),
            primaryText = Color(0xFFD8EAF2),
            mutedText = Color(0xFF6E8A9A),
            dotColor = Color(0xFF66F0FF),
            navTintActive = Color(0xFFAAD0DF),
            navTintInactive = Color(0xFF6F8C9D),
            navDivider = Color(0xFF254050),
            calendarDayBox = Color(0xFFE8F3F8),
            calendarDaySelected = Color(0xFF74ACCE),
            settingsTitleShadow = Color(0xFF345A70),
            danger = Color(0xFFFF7561),
            aliasHintBg = Color(0x2574ACCE),
            aliasHintBorder = Color(0x6674ACCE),
            aliasHintText = Color(0xFFD6EAF4),
        ),
        AppPalette(
            key = "alpine",
            name = "Alpine",
            appBgTop = Color(0xFF1C2735),
            appBgBottom = Color(0xFF131A26),
            primaryText = Color(0xFFE2E9F0),
            mutedText = Color(0xFF7E8D9E),
            dotColor = Color(0xFFB6D38C),
            navTintActive = Color(0xFFC3CEDB),
            navTintInactive = Color(0xFF7D8DA0),
            navDivider = Color(0xFF334457),
            calendarDayBox = Color(0xFFEDF1F6),
            calendarDaySelected = Color(0xFF8EA9C7),
            settingsTitleShadow = Color(0xFF455A73),
            danger = Color(0xFFFF7B67),
            aliasHintBg = Color(0x258EA9C7),
            aliasHintBorder = Color(0x668EA9C7),
            aliasHintText = Color(0xFFDEE8F3),
        ),
        AppPalette(
            key = "arch",
            name = "Arch",
            appBgTop = Color(0xFF1D2136),
            appBgBottom = Color(0xFF121626),
            primaryText = Color(0xFFDDE2F4),
            mutedText = Color(0xFF7982A2),
            dotColor = Color(0xFF00D0C9),
            navTintActive = Color(0xFFB6BEE0),
            navTintInactive = Color(0xFF747C9D),
            navDivider = Color(0xFF313A58),
            calendarDayBox = Color(0xFFE9ECF8),
            calendarDaySelected = Color(0xFF7D8DCB),
            settingsTitleShadow = Color(0xFF3E4A74),
            danger = Color(0xFFFF6F63),
            aliasHintBg = Color(0x257D8DCB),
            aliasHintBorder = Color(0x667D8DCB),
            aliasHintText = Color(0xFFD8DEF5),
        ),
        AppPalette(
            key = "aurora",
            name = "Aurora",
            appBgTop = Color(0xFF16263A),
            appBgBottom = Color(0xFF101A28),
            primaryText = Color(0xFFDDE8F6),
            mutedText = Color(0xFF7489A5),
            dotColor = Color(0xFF00E0A8),
            navTintActive = Color(0xFFB4C8E1),
            navTintInactive = Color(0xFF7085A1),
            navDivider = Color(0xFF2E4158),
            calendarDayBox = Color(0xFFEAF1FA),
            calendarDaySelected = Color(0xFF7EA8DA),
            settingsTitleShadow = Color(0xFF3D5878),
            danger = Color(0xFFFF7566),
            aliasHintBg = Color(0x257EA8DA),
            aliasHintBorder = Color(0x667EA8DA),
            aliasHintText = Color(0xFFD9E8F8),
        ),
        AppPalette(
            key = "beach",
            name = "Beach",
            appBgTop = Color(0xFF28304A),
            appBgBottom = Color(0xFF1A2138),
            primaryText = Color(0xFFECE6D8),
            mutedText = Color(0xFF9A9386),
            dotColor = Color(0xFFF2C96D),
            navTintActive = Color(0xFFD9D1C0),
            navTintInactive = Color(0xFF958E80),
            navDivider = Color(0xFF454D66),
            calendarDayBox = Color(0xFFF7F1E3),
            calendarDaySelected = Color(0xFFD3B47E),
            settingsTitleShadow = Color(0xFF5D6787),
            danger = Color(0xFFFF8A6E),
            aliasHintBg = Color(0x25D3B47E),
            aliasHintBorder = Color(0x66D3B47E),
            aliasHintText = Color(0xFFF2E8D0),
        ),
        AppPalette(
            key = "bliss",
            name = "Bliss",
            appBgTop = Color(0xFF2A243C),
            appBgBottom = Color(0xFF1A1528),
            primaryText = Color(0xFFECE4F5),
            mutedText = Color(0xFF9A8AAF),
            dotColor = Color(0xFFFF91C8),
            navTintActive = Color(0xFFD5C7E8),
            navTintInactive = Color(0xFF9789AC),
            navDivider = Color(0xFF473D5E),
            calendarDayBox = Color(0xFFF6EFFA),
            calendarDaySelected = Color(0xFFB294D6),
            settingsTitleShadow = Color(0xFF5D4E79),
            danger = Color(0xFFFF7D8D),
            aliasHintBg = Color(0x25B294D6),
            aliasHintBorder = Color(0x66B294D6),
            aliasHintText = Color(0xFFEDE2F7),
        ),
        AppPalette(
            key = "blueberry_dark",
            name = "Blueberry Dark",
            appBgTop = Color(0xFF1A1F3A),
            appBgBottom = Color(0xFF111629),
            primaryText = Color(0xFFE0E5F7),
            mutedText = Color(0xFF7A84A9),
            dotColor = Color(0xFF4AC8FF),
            navTintActive = Color(0xFFBBC4E8),
            navTintInactive = Color(0xFF7680A6),
            navDivider = Color(0xFF323B61),
            calendarDayBox = Color(0xFFECF0FA),
            calendarDaySelected = Color(0xFF7E92D4),
            settingsTitleShadow = Color(0xFF43508A),
            danger = Color(0xFFFF6F73),
            aliasHintBg = Color(0x257E92D4),
            aliasHintBorder = Color(0x667E92D4),
            aliasHintText = Color(0xFFDCE3F7),
        ),
        AppPalette(
            key = "carbon",
            name = "Carbon",
            appBgTop = Color(0xFF1D1F22),
            appBgBottom = Color(0xFF141517),
            primaryText = Color(0xFFE2E2E2),
            mutedText = Color(0xFF868686),
            dotColor = Color(0xFFFF9E4A),
            navTintActive = Color(0xFFC7C7C7),
            navTintInactive = Color(0xFF888888),
            navDivider = Color(0xFF37383B),
            calendarDayBox = Color(0xFFF0F0F0),
            calendarDaySelected = Color(0xFFA5A9AF),
            settingsTitleShadow = Color(0xFF56585C),
            danger = Color(0xFFFF6A5F),
            aliasHintBg = Color(0x25A5A9AF),
            aliasHintBorder = Color(0x66A5A9AF),
            aliasHintText = Color(0xFFE6E6E6),
        ),
        AppPalette(
            key = "cappuccino",
            name = "Cappuccino",
            appBgTop = Color(0xFF2D2522),
            appBgBottom = Color(0xFF1E1816),
            primaryText = Color(0xFFF0E5DB),
            mutedText = Color(0xFFA59588),
            dotColor = Color(0xFFDE9F72),
            navTintActive = Color(0xFFDCCCBD),
            navTintInactive = Color(0xFFA39082),
            navDivider = Color(0xFF4B3E37),
            calendarDayBox = Color(0xFFF8EFE6),
            calendarDaySelected = Color(0xFFCBA483),
            settingsTitleShadow = Color(0xFF6A564B),
            danger = Color(0xFFFF7A66),
            aliasHintBg = Color(0x25CBA483),
            aliasHintBorder = Color(0x66CBA483),
            aliasHintText = Color(0xFFF2E6DA),
        ),
    )

    fun byKey(key: String): AppPalette {
        val normalized = key.trim().lowercase()
        return all.firstOrNull { it.key == normalized } ?: all.first { it.key == defaultKey }
    }

    fun nextKey(currentKey: String): String {
        val idx = all.indexOfFirst { it.key == currentKey.trim().lowercase() }.let { if (it < 0) 0 else it }
        return all[(idx + 1) % all.size].key
    }
}

private val ActivePaletteState = mutableStateOf(ThemePalettes.byKey(ThemePalettes.defaultKey))
private val AppBgTop: Color get() = ActivePaletteState.value.appBgTop
private val AppBgBottom: Color get() = ActivePaletteState.value.appBgBottom
private val PrimaryText: Color get() = ActivePaletteState.value.primaryText
private val MutedText: Color get() = ActivePaletteState.value.mutedText
private val DotColor: Color get() = ActivePaletteState.value.dotColor
private val NavTintActive: Color get() = ActivePaletteState.value.navTintActive
private val NavTintInactive: Color get() = ActivePaletteState.value.navTintInactive
private val NavDivider: Color get() = ActivePaletteState.value.navDivider
private val CalendarDayBox: Color get() = ActivePaletteState.value.calendarDayBox
private val CalendarDaySelected: Color get() = ActivePaletteState.value.calendarDaySelected
private val SettingsTitleShadow: Color get() = ActivePaletteState.value.settingsTitleShadow
private val Danger: Color get() = ActivePaletteState.value.danger
private val AliasHintBg: Color get() = ActivePaletteState.value.aliasHintBg
private val AliasHintBorder: Color get() = ActivePaletteState.value.aliasHintBorder
private val AliasHintText: Color get() = ActivePaletteState.value.aliasHintText
private val InterVariable = FontFamily(
    Font(R.font.inter_variable, FontWeight.Normal),
    Font(R.font.inter_variable, FontWeight.Medium),
    Font(R.font.inter_variable, FontWeight.Bold),
    Font(R.font.inter_variable, FontWeight.ExtraBold),
    Font(R.font.inter_variable, FontWeight.Black),
    Font(R.font.inter_italic_variable, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.inter_italic_variable, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.inter_italic_variable, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.inter_italic_variable, FontWeight.Black, FontStyle.Italic),
)
private val InterVariableBlack = FontFamily(
    Font(R.font.inter_black, FontWeight.Black)
)

// Global font map: update these values to switch fonts app-wide.
private object AppFonts {
    val screenTitle = InterVariable
    val amount = InterVariableBlack
    val input = FontFamily.Monospace
    val rowLabel = FontFamily.Monospace
    val rowValue = FontFamily.Monospace
    val nav = FontFamily.Monospace
    val helper = FontFamily.Monospace
    val settingsTitle = InterVariableBlack
    val settingsRow = FontFamily.Monospace
    val settingsValue = FontFamily.Monospace
}

// Global layout tokens
private object LayoutUi {
    val horizontalPadding = 25.dp
}

// Khatas tab tokens
private object KhatasUi {
    val topSpacing = 40.dp
    val totalSize = 62.sp
    val totalLetterSpacing = (-2.5).sp
    val totalWeight = FontWeight.Black
    val inputTopSpacing = 40.dp
    val inputSize = 30.sp
    val placeholderSize = 30.sp
    val statusSize = 13.sp
    val hintTopSpacing = 8.dp
    val hintCorner = 999.dp
    val hintTextSize = 12.sp
    val hintHorizontalPadding = 10.dp
    val hintVerticalPadding = 5.dp
    val listTopSpacing = 30.dp
    val placeholderStartDelayMs = 2000L
    val placeholderHoldMs = 2000L
    val placeholderTypeMs = 62L
    val placeholderDeleteMs = 36L
    val placeholderGapMs = 180L
    val placeholderSamples = listOf(
        "10 chai",
        "20 biscuit",
        "50 petrol",
        "15 lemon",
        "30 samosa",
    )
}

// Calendar tab tokens
private object CalendarUi {
    val topSpacing = 40.dp
    val totalSize = 62.sp
    val totalLetterSpacing = (-2.5).sp
    val totalWeight = FontWeight.Black
    val afterTotalSpacing = 20.dp
    val monthTitleSize = 42.sp
    val monthTitleBottomSpacing = 10.dp
    val monthNavIconSize = 20.dp
    val monthNavButtonSize = 40.dp
    val monthNavSpacing = 24.dp
    val monthNavPaddingTop = 0.dp
    val monthNavPaddingBottom = 8.dp
    val monthNavButtonBg = Color(0x182A2E44)
    val monthNavButtonPressedBg = Color(0x505976A7)
    val tableWidthFraction = 0.80f
    val weekRowBottom = 10.dp
    val weekGap = 6.dp
    val weekLabelSize = 13.sp
    val weekLabelColor = Color(0xFF32364B)
    val dayCellCorner = 8.dp
    val dayCellBottom = 6.dp
    val dayTextSize = 13.sp
    val dayTextWithEntry = Color(0xFF1C2132)
    val dayTextWithoutEntry = Color(0xFF70758A)
    val dayTextInRange = Color(0xFFFFFFFF)
    val dayCellInRange = Color(0x667B93BD)
    val dayAnimMs = 140
    val dayScaleSelected = 1.03f
    val dayScaleRange = 1.015f
    val dayScaleNormal = 1.0f
    val resultsTopSpacing = 10.dp
    val emptyStateSize = 15.sp
    val emptyStateTopMargin = 24.dp
}

// Share tab tokens
private object ShareUi {
    val topSpacing = 18.dp
    val titleBottom = 24.dp
    val sectionGap = 18.dp
    val metricLabelSize = 12.sp
    val metricValueSize = 15.sp
    val metricValueWeight = FontWeight.Bold
    val metricRowGap = 2.dp
    val listTop = 8.dp
    val listRowVertical = 2.dp
    val itemNameSize = 15.sp
    val itemValueSize = 15.sp
    val emptySize = 14.sp
    val sectionDividerTop = 14.dp
    val sectionDividerBottom = 12.dp
    val sectionDividerHeight = 1.dp
    val sectionDividerColor: Color get() = NavDivider
}

// Entry and budget list tokens
private object ListUi {
    val entryRowVerticalPadding = 2.dp
    val swipeRowMinHeight = 44.dp
    val bulletSize = 18.sp
    val bulletToText = 10.dp
    val noteSize = 15.sp
    val amountSize = 15.sp
    val budgetRowVerticalPadding = 2.dp
    val budgetDetailSize = 12.sp
    val swipeHintThresholdRatio = 0.4f
    val swipeCommitThresholdRatio = 0.5f
    val swipeMaxOffsetRatio = 0.9f
    val swipeDeleteBg = Color(0x332F1014)
    val swipeEditBg = Color(0x3321342B)
    val swipeDeleteIcon = Color(0xFFFF7A7A)
    val swipeEditIcon = Color(0xFF93D8B5)
    val swipeHintAnimMs = 140
    val editRowGap = 8.dp
    val editInputSize = 14.sp
}

// Bottom navigation tokens
private object NavUi {
    val bottomPadding = 2.dp
    val dividerHeight = 1.dp
    val rowTopPadding = 8.dp
    val rowBottomPadding = 6.dp
    val itemVerticalPadding = 4.dp
    val iconSize = 20.dp
    val labelTopSpacing = 4.dp
    val labelSize = 12.sp
    val labelLetterSpacing = 0.5.sp
}

// Settings screen style tokens: tweak these values to quickly restyle the whole section.
private object SettingsUi {
    val backIconSize = 22.dp
    val titleSize = 60.sp
    val titleLetterSpacing = (-2.5).sp
    val titleWeight = FontWeight.Black
    val titleFont = AppFonts.settingsTitle
    val rowFont = AppFonts.settingsRow
    val trailingFont = AppFonts.settingsValue
    val rowLabelSize = 16.sp
    val trailingValueSize = 16.sp
    val rowHorizontalPadding = 6.dp
    val rowTouchPaddingVertical = 8.dp
    val rowVerticalPadding = 10.dp
    val actionIconSize = 26.dp
    val statusSize = 15.sp
    val versionSize = 10.sp
}

private object ToastUi {
    val horizontalPadding = 40.dp
    val bottomPadding = 90.dp
    val corner = 999.dp
    val borderWidth = 1.dp
    val messageHorizontalPadding = 16.dp
    val messageVerticalPadding = 9.dp
    val messageSize = 13.sp
    val messageFont = AppFonts.rowLabel
    val normalBg = Color(0x28D8DADF)
    val normalBorder = Color(0x55D8DADF)
    val normalText = Color(0xFFE4E6EC)
    val errorBg = Color(0x33FF5A63)
    val errorBorder = Color(0x66FF7A83)
    val errorText = Color(0xFFFFD2D7)
    val enterFadeMs = 180
    val enterSlideMs = 220
    val exitFadeMs = 180
    val exitSlideMs = 220
    val visibleMs = 1700L
    val gapAfterExitMs = 220L
    val enterSlideOffsetDivisor = 2
    val exitSlideOffsetDivisor = 2
}

// Haptic feedback config: tune this block to control feel globally.
private object HapticUi {
    val tapType = HapticFeedbackType.LongPress
    val dragType = HapticFeedbackType.LongPress
    val scrollType = HapticFeedbackType.TextHandleMove
    val tapRepeats = 2
    val dragRepeats = 1
    val scrollRepeats = 1
    val pulseGapMs = 12L
    val scrollStep = 52.dp
}

private data class ToastMessage(
    val id: Long,
    val text: String,
    val isError: Boolean = false,
)

@Composable
fun ExpenseApp(gateway: ExpenseGateway) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val density = LocalDensity.current
    val khatasScrollState = rememberScrollState()
    val calendarScrollState = rememberScrollState()
    val scrollStepPx = with(density) { HapticUi.scrollStep.toPx() }
    val hapticScope = rememberCoroutineScope()
    fun triggerHaptic(type: HapticFeedbackType, repeats: Int) {
        hapticScope.launch {
            val count = repeats.coerceAtLeast(1)
            repeat(count) { idx ->
                haptics.performHapticFeedback(type)
                if (idx < count - 1) delay(HapticUi.pulseGapMs)
            }
        }
    }
    fun hapticTap() = triggerHaptic(HapticUi.tapType, HapticUi.tapRepeats)
    fun hapticDragTick() = triggerHaptic(HapticUi.dragType, HapticUi.dragRepeats)
    fun hapticScrollTick() = triggerHaptic(HapticUi.scrollType, HapticUi.scrollRepeats)

    var tab by remember { mutableStateOf(Tab.KHATAS) }
    var command by remember { mutableStateOf("") }
    var search by remember { mutableStateOf("") }
    var budgetLabel by remember { mutableStateOf("") }
    var budgetAmount by remember { mutableStateOf("") }
    var currencyCode by remember { mutableStateOf(gateway.currencyCode()) }
    var themePaletteKey by remember { mutableStateOf(gateway.themePaletteKey()) }

    var recent by remember { mutableStateOf(gateway.recentEntries(20u)) }
    var allEntries by remember { mutableStateOf(gateway.allEntries("", 200u)) }
    var totalMinor by remember { mutableStateOf(gateway.todayTotalMinor()) }
    var monthMinor by remember { mutableStateOf(gateway.monthTotalMinor()) }
    var budgets by remember { mutableStateOf(gateway.budgets()) }
    var diagnostics by remember { mutableStateOf(gateway.diagnostics()) }
    var calendarEntries by remember { mutableStateOf(gateway.allEntries("", 1000u)) }
    var calendarMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDay by remember { mutableStateOf(LocalDate.now()) }
    var selectedRangeStart by remember { mutableStateOf<LocalDate?>(null) }
    var selectedRangeEnd by remember { mutableStateOf<LocalDate?>(null) }
    var editingEntryId by remember { mutableStateOf<String?>(null) }
    var editDraft by remember { mutableStateOf("") }
    var toast by remember { mutableStateOf<ToastMessage?>(null) }
    var toastVisible by remember { mutableStateOf(false) }

    fun showToast(message: String, isError: Boolean = false) {
        toast = ToastMessage(id = System.nanoTime(), text = message, isError = isError)
    }

    fun refreshAll() {
        recent = gateway.recentEntries(20u)
        allEntries = gateway.allEntries(search, 200u)
        calendarEntries = gateway.allEntries("", 1000u)
        totalMinor = gateway.todayTotalMinor()
        monthMinor = gateway.monthTotalMinor()
        budgets = gateway.budgets()
        currencyCode = gateway.currencyCode()
        themePaletteKey = gateway.themePaletteKey()
        diagnostics = gateway.diagnostics()
    }

    val activePalette = ThemePalettes.byKey(themePaletteKey)
    LaunchedEffect(activePalette.key) {
        ActivePaletteState.value = activePalette
    }

    fun writeExportJsonToUri(uri: Uri) {
        val exportPath = gateway.exportJson()
        val source = File(exportPath)
        context.contentResolver.openOutputStream(uri, "wt")?.use { out ->
            source.inputStream().use { input -> input.copyTo(out) }
        } ?: throw IllegalStateException("Output stream unavailable")
    }

    fun importJsonFromUri(uri: Uri) {
        val target = File(context.filesDir, "exports/transactions.json")
        target.parentFile?.mkdirs()
        context.contentResolver.openInputStream(uri)?.use { input ->
            target.outputStream().use { output -> input.copyTo(output) }
        } ?: throw IllegalStateException("Input stream unavailable")
        gateway.importJson()
        refreshAll()
    }

    val exportFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        try {
            writeExportJsonToUri(uri)
            showToast("Exported successfully")
        } catch (_: Throwable) {
            showToast("Error occurred", isError = true)
        }
    }

    val importFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        try {
            importJsonFromUri(uri)
            showToast("Imported successfully")
        } catch (_: Throwable) {
            showToast("Error occurred", isError = true)
        }
    }

    LaunchedEffect(tab, khatasScrollState, scrollStepPx) {
        if (tab != Tab.KHATAS) return@LaunchedEffect
        var prev = khatasScrollState.value
        var acc = 0f
        snapshotFlow { khatasScrollState.value }.collect { current ->
            val delta = (current - prev).toFloat()
            prev = current
            acc += delta
            if (abs(acc) >= scrollStepPx) {
                hapticScrollTick()
                acc = 0f
            }
        }
    }

    LaunchedEffect(tab, calendarScrollState, scrollStepPx) {
        if (tab != Tab.CALENDAR) return@LaunchedEffect
        var prev = calendarScrollState.value
        var acc = 0f
        snapshotFlow { calendarScrollState.value }.collect { current ->
            val delta = (current - prev).toFloat()
            prev = current
            acc += delta
            if (abs(acc) >= scrollStepPx) {
                hapticScrollTick()
                acc = 0f
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AppBgTop, AppBgBottom)))
    ) {
        LaunchedEffect(toast?.id) {
            if (toast != null) {
                toastVisible = true
                delay(ToastUi.visibleMs)
                toastVisible = false
                delay(ToastUi.gapAfterExitMs)
                toast = null
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = LayoutUi.horizontalPadding)
        ) {
            when (tab) {
                Tab.KHATAS -> {
                    Spacer(Modifier.height(KhatasUi.topSpacing))
                    Text(
                        text = formatCurrency(totalMinor, currencyCode),
                        color = PrimaryText,
                        fontFamily = AppFonts.amount,
                        fontWeight = KhatasUi.totalWeight,
                        fontSize = KhatasUi.totalSize,
                        letterSpacing = KhatasUi.totalLetterSpacing,
                    )

                    Spacer(Modifier.height(KhatasUi.inputTopSpacing))
                    QuickEntryInput(
                        value = command,
                        onValueChange = { newValue -> command = newValue },
                        placeholderSize = KhatasUi.placeholderSize,
                        inputSize = KhatasUi.inputSize,
                        onDone = {
                            val trimmed = command.trim()
                            if (trimmed.isNotEmpty()) {
                                try {
                                    hapticTap()
                                    gateway.addQuickEntry(QuickAliasDictionary.expandQuickInput(trimmed))
                                    command = ""
                                    showToast("Saved")
                                    refreshAll()
                                } catch (t: Throwable) {
                                    showToast(t.message ?: "Invalid input", isError = true)
                                }
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(khatasScrollState)
                        ) {
                            Spacer(Modifier.height(KhatasUi.listTopSpacing))
                            recent.forEach { entry ->
                                if (editingEntryId == entry.id) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = ListUi.entryRowVerticalPadding),
                                        horizontalArrangement = Arrangement.spacedBy(ListUi.editRowGap),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        BasicTextField(
                                            value = editDraft,
                                            onValueChange = { editDraft = it },
                                            modifier = Modifier.weight(1f),
                                            textStyle = TextStyle(
                                                color = PrimaryText,
                                                fontFamily = AppFonts.input,
                                                fontSize = ListUi.editInputSize
                                            ),
                                            singleLine = true,
                                            cursorBrush = SolidColor(PrimaryText),
                                        )
                                        Button(onClick = {
                                            val id = editingEntryId
                                            val input = editDraft.trim()
                                            if (id != null && input.isNotEmpty()) {
                                                try {
                                                    hapticTap()
                                                    gateway.updateEntryQuick(id, QuickAliasDictionary.expandQuickInput(input))
                                                    editingEntryId = null
                                                    editDraft = ""
                                                    showToast("Entry updated")
                                                    refreshAll()
                                                } catch (t: Throwable) {
                                                    showToast(t.message ?: "Unable to update", isError = true)
                                                }
                                            }
                                        }) { Text("Save") }
                                        Button(onClick = {
                                            hapticTap()
                                            editingEntryId = null
                                            editDraft = ""
                                        }) { Text("Cancel") }
                                    }
                                } else {
                                    SwipeableEntryRow(
                                        entry = entry,
                                        currency = currencyCode,
                                        onDeleteHint = { hapticDragTick() },
                                        onEditHint = { hapticDragTick() },
                                        onDelete = {
                                            hapticTap()
                                            gateway.deleteEntry(entry.id)
                                            showToast("Entry deleted")
                                            refreshAll()
                                        },
                                        onEdit = {
                                            hapticTap()
                                            editingEntryId = entry.id
                                            editDraft = quickInputFromEntry(entry)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Tab.CALENDAR -> {
                    if (selectedDay.year != calendarMonth.year || selectedDay.month != calendarMonth.month) {
                        selectedDay = calendarMonth.atDay(1)
                    }
                    val normalizedRange = if (selectedRangeStart != null && selectedRangeEnd != null) {
                        normalizeRange(selectedRangeStart!!, selectedRangeEnd!!)
                    } else {
                        null
                    }
                    val selectedEntries = if (normalizedRange != null) {
                        calendarEntries.filter {
                            val d = parseCreatedAtDate(it.createdAt)
                            d >= normalizedRange.first && d <= normalizedRange.second
                        }
                    } else {
                        calendarEntries.filter { parseCreatedAtDate(it.createdAt) == selectedDay }
                    }
                    val selectedTotal = selectedEntries.sumOf { it.amountMinor }
                    val monthEntryDays = calendarEntries
                        .map { parseCreatedAtDate(it.createdAt) }
                        .filter { it.year == calendarMonth.year && it.month == calendarMonth.month }
                        .map { it.dayOfMonth }
                        .toSet()
                    val monthCells = buildMonthCells(calendarMonth)

                    Spacer(Modifier.height(CalendarUi.topSpacing))
                    Text(
                        text = formatCurrency(selectedTotal, currencyCode),
                        color = PrimaryText,
                        fontFamily = AppFonts.amount,
                        fontWeight = CalendarUi.totalWeight,
                        fontSize = CalendarUi.totalSize,
                        letterSpacing = CalendarUi.totalLetterSpacing,
                    )
                    Spacer(Modifier.height(CalendarUi.afterTotalSpacing))

                    Text(
                        text = calendarMonth.month.getDisplayName(DateTextStyle.SHORT, Locale.ENGLISH).lowercase(Locale.ENGLISH),
                        color = PrimaryText,
                        fontFamily = AppFonts.screenTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = CalendarUi.monthTitleSize
                    )
                    Spacer(Modifier.height(CalendarUi.monthTitleBottomSpacing))
                    Row(
                        modifier = Modifier.padding(top = CalendarUi.monthNavPaddingTop, bottom = CalendarUi.monthNavPaddingBottom),
                        horizontalArrangement = Arrangement.spacedBy(CalendarUi.monthNavSpacing),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundArrowButton(
                            icon = Icons.Outlined.ChevronLeft,
                            contentDescription = "Previous month",
                            onClick = {
                                hapticTap()
                                calendarMonth = calendarMonth.minusMonths(1)
                            }
                        )
                        RoundArrowButton(
                            icon = Icons.Outlined.ChevronRight,
                            contentDescription = "Next month",
                            onClick = {
                                hapticTap()
                                calendarMonth = calendarMonth.plusMonths(1)
                            }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(CalendarUi.tableWidthFraction)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = CalendarUi.weekRowBottom),
                        horizontalArrangement = Arrangement.spacedBy(CalendarUi.weekGap)
                    ) {
                        listOf("M", "T", "W", "T", "F", "S", "S").forEach { d ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    d,
                                    color = CalendarUi.weekLabelColor,
                                    fontFamily = AppFonts.helper,
                                    fontSize = CalendarUi.weekLabelSize
                                )
                            }
                        }
                    }
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth(CalendarUi.tableWidthFraction)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        val density = LocalDensity.current
                        val gapPx = with(density) { CalendarUi.weekGap.toPx() }
                        val widthPx = with(density) { maxWidth.toPx() }
                        val cellPx = ((widthPx - (gapPx * 6f)) / 7f).coerceAtLeast(1f)
                        val rows = (monthCells.size / 7).coerceAtLeast(1)
                        val rowStep = cellPx + gapPx

                        Column(
                            modifier = Modifier
                                .pointerInput(monthCells, rows, rowStep, cellPx, gapPx) {
                                    detectTapGestures(
                                        onTap = { offset ->
                                            val date = monthDateAtOffset(
                                                x = offset.x,
                                                y = offset.y,
                                                monthCells = monthCells,
                                                cellPx = cellPx,
                                                gapPx = gapPx,
                                                rows = rows,
                                                rowStep = rowStep
                                            )
                                            if (date != null) {
                                                hapticTap()
                                                selectedDay = date
                                                selectedRangeStart = null
                                                selectedRangeEnd = null
                                            }
                                        }
                                    )
                                }
                                .pointerInput(monthCells, rows, rowStep, cellPx, gapPx) {
                                    var moved = false
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = { offset ->
                                            moved = false
                                            val startDate = monthDateAtOffset(
                                                x = offset.x,
                                                y = offset.y,
                                                monthCells = monthCells,
                                                cellPx = cellPx,
                                                gapPx = gapPx,
                                                rows = rows,
                                                rowStep = rowStep
                                            )
                                            if (startDate != null) {
                                                hapticTap()
                                                selectedRangeStart = startDate
                                                selectedRangeEnd = startDate
                                            }
                                        },
                                        onDrag = { change, _ ->
                                            val start = selectedRangeStart
                                            if (start != null) {
                                                val previous = selectedRangeEnd
                                                val date = monthDateAtOffset(
                                                    x = change.position.x,
                                                    y = change.position.y,
                                                    monthCells = monthCells,
                                                    cellPx = cellPx,
                                                    gapPx = gapPx,
                                                    rows = rows,
                                                    rowStep = rowStep
                                                )
                                                if (date != null && date != previous) {
                                                    moved = true
                                                    selectedRangeEnd = date
                                                    hapticDragTick()
                                                }
                                                change.consume()
                                            }
                                        },
                                        onDragCancel = {
                                            if (!moved && selectedRangeStart != null) {
                                                selectedDay = selectedRangeStart!!
                                                selectedRangeStart = null
                                                selectedRangeEnd = null
                                            }
                                        },
                                        onDragEnd = {
                                            if (!moved && selectedRangeStart != null) {
                                                selectedDay = selectedRangeStart!!
                                                selectedRangeStart = null
                                                selectedRangeEnd = null
                                            }
                                        }
                                    )
                                }
                        ) {
                            monthCells.chunked(7).forEach { week ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = CalendarUi.dayCellBottom),
                                    horizontalArrangement = Arrangement.spacedBy(CalendarUi.weekGap)
                                ) {
                                    week.forEach { date ->
                                        if (date == null) {
                                            Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                                        } else {
                                            val dayHasEntries = monthEntryDays.contains(date.dayOfMonth)
                                            val isSelected = date == selectedDay && normalizedRange == null
                                            val inRange = normalizedRange?.let { date >= it.first && date <= it.second } == true
                                            val targetBg = when {
                                                inRange -> CalendarUi.dayCellInRange
                                                isSelected -> CalendarDaySelected
                                                else -> CalendarDayBox
                                            }
                                            val animatedBg by animateColorAsState(
                                                targetValue = targetBg,
                                                animationSpec = tween(CalendarUi.dayAnimMs),
                                                label = "calendar_day_bg"
                                            )
                                            val targetScale = when {
                                                isSelected -> CalendarUi.dayScaleSelected
                                                inRange -> CalendarUi.dayScaleRange
                                                else -> CalendarUi.dayScaleNormal
                                            }
                                            val animatedScale by animateFloatAsState(
                                                targetValue = targetScale,
                                                animationSpec = tween(CalendarUi.dayAnimMs),
                                                label = "calendar_day_scale"
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .aspectRatio(1f)
                                                    .graphicsLayer {
                                                        scaleX = animatedScale
                                                        scaleY = animatedScale
                                                    }
                                                    .background(
                                                        animatedBg,
                                                        RoundedCornerShape(CalendarUi.dayCellCorner)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = date.dayOfMonth.toString(),
                                                    color = when {
                                                        inRange -> CalendarUi.dayTextInRange
                                                        dayHasEntries -> CalendarUi.dayTextWithEntry
                                                        else -> CalendarUi.dayTextWithoutEntry
                                                    },
                                                    fontFamily = AppFonts.helper,
                                                    fontSize = CalendarUi.dayTextSize
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(CalendarUi.resultsTopSpacing))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(calendarScrollState)
                        ) {
                            if (selectedEntries.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = CalendarUi.emptyStateTopMargin),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    Text(
                                        text = "No entries",
                                        color = MutedText,
                                        fontFamily = AppFonts.helper,
                                        fontSize = CalendarUi.emptyStateSize
                                    )
                                }
                            } else {
                                selectedEntries.forEach { entry ->
                                    EntryRow(entry, currencyCode)
                                }
                            }
                        }
                    }
                }

                Tab.SHARE -> {
                    Spacer(Modifier.height(CalendarUi.topSpacing))
                    Text(
                        "Insights",
                        color = SettingsTitleShadow,
                        fontFamily = SettingsUi.titleFont,
                        fontSize = SettingsUi.titleSize,
                        fontWeight = SettingsUi.titleWeight,
                        letterSpacing = SettingsUi.titleLetterSpacing
                    )
                    Spacer(Modifier.height(ShareUi.titleBottom))
                    val today = LocalDate.now()
                    val thisWeekStart = today.minusDays(6)
                    val lastWeekEnd = thisWeekStart.minusDays(1)
                    val lastWeekStart = lastWeekEnd.minusDays(6)
                    val thisWeekTotal = allEntries
                        .filter {
                            val d = parseCreatedAtDate(it.createdAt)
                            d >= thisWeekStart && d <= today
                        }
                        .sumOf { it.amountMinor }
                    val lastWeekTotal = allEntries
                        .filter {
                            val d = parseCreatedAtDate(it.createdAt)
                            d >= lastWeekStart && d <= lastWeekEnd
                        }
                        .sumOf { it.amountMinor }
                    val weekChangePercent = if (lastWeekTotal == 0L) {
                        if (thisWeekTotal == 0L) 0.0 else 100.0
                    } else {
                        ((thisWeekTotal - lastWeekTotal).toDouble() / lastWeekTotal.toDouble()) * 100.0
                    }
                    val topItems = allEntries
                        .groupBy { it.note.trim().ifBlank { "untitled" } }
                        .mapValues { (_, entries) -> entries.sumOf { it.amountMinor } }
                        .entries
                        .sortedByDescending { it.value }
                        .take(5)

                    MetricRow(
                        label = "This week",
                        value = formatCurrency(thisWeekTotal, currencyCode),
                    )
                    Spacer(Modifier.height(ShareUi.metricRowGap))
                    MetricRow(
                        label = "Last week",
                        value = formatCurrency(lastWeekTotal, currencyCode),
                    )
                    Spacer(Modifier.height(ShareUi.metricRowGap))
                    MetricRow(
                        label = "7d trend",
                        value = String.format(Locale.US, "%+.1f%%", weekChangePercent),
                    )
                    Spacer(Modifier.height(ShareUi.metricRowGap))
                    MetricRow(
                        label = "This month",
                        value = formatCurrency(monthMinor, currencyCode),
                    )
                    Spacer(Modifier.height(ShareUi.sectionDividerTop))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(ShareUi.sectionDividerHeight)
                            .background(ShareUi.sectionDividerColor)
                    )
                    Spacer(Modifier.height(ShareUi.sectionDividerBottom))
                    Text(
                        text = "Top items",
                        color = MutedText,
                        fontFamily = AppFonts.helper,
                        fontSize = ShareUi.metricLabelSize
                    )
                    Spacer(Modifier.height(ShareUi.listTop))
                    if (topItems.isEmpty()) {
                        Text(
                            text = "No entries",
                            color = MutedText,
                            fontFamily = AppFonts.helper,
                            fontSize = ShareUi.emptySize
                        )
                    } else {
                        topItems.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = ShareUi.listRowVertical),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.key,
                                    color = PrimaryText,
                                    fontFamily = AppFonts.rowLabel,
                                    fontSize = ShareUi.itemNameSize
                                )
                                Text(
                                    text = formatCurrency(item.value, currencyCode),
                                    color = PrimaryText,
                                    fontFamily = AppFonts.rowValue,
                                    fontSize = ShareUi.itemValueSize
                                )
                            }
                        }
                    }
                }

                Tab.SETTINGS -> {
                    Spacer(Modifier.height(CalendarUi.topSpacing))
                    Text(
                        "Settings",
                        color = SettingsTitleShadow,
                        fontFamily = SettingsUi.titleFont,
                        fontSize = SettingsUi.titleSize,
                        fontWeight = SettingsUi.titleWeight,
                        letterSpacing = SettingsUi.titleLetterSpacing
                    )
                    Spacer(Modifier.height(24.dp))
                    SettingRow(
                        title = "Currency",
                        danger = false,
                        onClick = {
                            hapticTap()
                            val next = when (currencyCode.uppercase()) {
                                "INR" -> "USD"
                                "USD" -> "EUR"
                                else -> "INR"
                            }
                            gateway.setCurrencyCode(next)
                            refreshAll()
                            showToast("Currency: $next")
                        }
                    ) {
                        Text(
                            text = currencySymbol(currencyCode),
                            color = PrimaryText,
                            fontFamily = SettingsUi.trailingFont,
                            fontSize = SettingsUi.trailingValueSize
                        )
                    }
                    SettingRow(
                        title = "Theme",
                        danger = false,
                        onClick = {
                            hapticTap()
                            val next = ThemePalettes.nextKey(themePaletteKey)
                            gateway.setThemePaletteKey(next)
                            refreshAll()
                            showToast("Theme: ${ThemePalettes.byKey(next).name}")
                        }
                    ) {
                        Text(
                            text = activePalette.name,
                            color = PrimaryText,
                            fontFamily = SettingsUi.trailingFont,
                            fontSize = SettingsUi.trailingValueSize
                        )
                    }
                    SettingRow(
                        title = "Export Data (JSON)",
                        danger = false,
                        onClick = {
                            hapticTap()
                            exportFileLauncher.launch("transactions.json")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = "Export",
                            tint = PrimaryText,
                            modifier = Modifier.size(SettingsUi.actionIconSize)
                        )
                    }
                    SettingRow(
                        title = "Import Data",
                        danger = false,
                        onClick = {
                            hapticTap()
                            importFileLauncher.launch(arrayOf("application/json", "text/plain"))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Upload,
                            contentDescription = "Import",
                            tint = PrimaryText,
                            modifier = Modifier.size(SettingsUi.actionIconSize)
                        )
                    }
                    SettingRow(
                        title = "Clear All Data",
                        danger = true,
                        onClick = {
                            hapticTap()
                            gateway.clearAllData()
                            refreshAll()
                            showToast("All data cleared")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = "Clear",
                            tint = Danger,
                            modifier = Modifier.size(SettingsUi.actionIconSize)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "v1.0.2",
                        color = Color(0xFF4A4E64),
                        fontFamily = SettingsUi.trailingFont,
                        fontSize = SettingsUi.versionSize,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }

            if (tab == Tab.SHARE || tab == Tab.SETTINGS) {
                Spacer(Modifier.weight(1f))
            }
            BottomNav(
                current = tab,
                onSelect = {
                    hapticTap()
                    tab = it
                }
            )
        }

        AnimatedVisibility(
            visible = toastVisible && toast != null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ToastUi.horizontalPadding, vertical = ToastUi.bottomPadding),
            enter = fadeIn(animationSpec = tween(ToastUi.enterFadeMs)) + slideInVertically(
                animationSpec = tween(ToastUi.enterSlideMs),
                initialOffsetY = { it / ToastUi.enterSlideOffsetDivisor }
            ),
            exit = fadeOut(animationSpec = tween(ToastUi.exitFadeMs)) + slideOutVertically(
                animationSpec = tween(ToastUi.exitSlideMs),
                targetOffsetY = { it / ToastUi.exitSlideOffsetDivisor }
            )
        ) {
            toast?.let { message ->
                MinimalToast(message)
            }
        }
    }
}

@Composable
private fun MinimalToast(message: ToastMessage) {
    val bg = if (message.isError) ToastUi.errorBg else ToastUi.normalBg
    val border = if (message.isError) ToastUi.errorBorder else ToastUi.normalBorder
    val fg = if (message.isError) ToastUi.errorText else ToastUi.normalText
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(ToastUi.corner))
            .border(ToastUi.borderWidth, border, RoundedCornerShape(ToastUi.corner))
            .padding(horizontal = ToastUi.messageHorizontalPadding, vertical = ToastUi.messageVerticalPadding)
    ) {
        Text(
            text = message.text,
            color = fg,
            fontFamily = ToastUi.messageFont,
            fontSize = ToastUi.messageSize
        )
    }
}

@Composable
private fun SettingRow(
    title: String,
    danger: Boolean,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SettingsUi.rowTouchPaddingVertical)
            .padding(
                horizontal = SettingsUi.rowHorizontalPadding,
                vertical = SettingsUi.rowVerticalPadding
            )
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = if (danger) Danger else PrimaryText,
            fontFamily = SettingsUi.rowFont,
            fontSize = SettingsUi.rowLabelSize,
            fontWeight = FontWeight.Medium
        )
        trailing()
    }
}

@Composable
private fun MetricRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ShareUi.listRowVertical),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("\u2022", color = DotColor, fontSize = ListUi.bulletSize)
            Spacer(Modifier.width(ListUi.bulletToText))
            Text(
                text = label,
                color = PrimaryText,
                fontFamily = AppFonts.rowLabel,
                fontSize = ShareUi.itemNameSize
            )
        }
        Text(
            text = value,
            color = PrimaryText,
            fontFamily = AppFonts.rowValue,
            fontSize = ShareUi.metricValueSize,
            fontWeight = ShareUi.metricValueWeight
        )
    }
}

@Composable
private fun SwipeableEntryRow(
    entry: ExpenseEntry,
    currency: String,
    onDeleteHint: () -> Unit,
    onEditHint: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    val density = LocalDensity.current
    var offsetX by remember(entry.id) { mutableStateOf(0f) }
    var deleteHintFired by remember(entry.id) { mutableStateOf(false) }
    var editHintFired by remember(entry.id) { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ListUi.entryRowVerticalPadding)
    ) {
        val rowWidthPx = with(density) { maxWidth.toPx() }
        val hintThresholdPx = rowWidthPx * ListUi.swipeHintThresholdRatio
        val commitThresholdPx = rowWidthPx * ListUi.swipeCommitThresholdRatio
        val maxOffsetPx = rowWidthPx * ListUi.swipeMaxOffsetRatio

        val showDeleteHint = offsetX <= -hintThresholdPx
        val showEditHint = offsetX >= hintThresholdPx
        val deleteIconAlpha by animateFloatAsState(
            targetValue = if (showDeleteHint) 1f else 0f,
            animationSpec = tween(ListUi.swipeHintAnimMs),
            label = "delete_hint_alpha"
        )
        val editIconAlpha by animateFloatAsState(
            targetValue = if (showEditHint) 1f else 0f,
            animationSpec = tween(ListUi.swipeHintAnimMs),
            label = "edit_hint_alpha"
        )
        val deleteIconScale by animateFloatAsState(
            targetValue = if (showDeleteHint) 1f else 0.88f,
            animationSpec = tween(ListUi.swipeHintAnimMs),
            label = "delete_hint_scale"
        )
        val editIconScale by animateFloatAsState(
            targetValue = if (showEditHint) 1f else 0.88f,
            animationSpec = tween(ListUi.swipeHintAnimMs),
            label = "edit_hint_scale"
        )

        LaunchedEffect(showDeleteHint) {
            if (showDeleteHint && !deleteHintFired) {
                onDeleteHint()
                deleteHintFired = true
            }
            if (!showDeleteHint) deleteHintFired = false
        }
        LaunchedEffect(showEditHint) {
            if (showEditHint && !editHintFired) {
                onEditHint()
                editHintFired = true
            }
            if (!showEditHint) editHintFired = false
        }

        if (showDeleteHint) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(ListUi.swipeDeleteBg, RoundedCornerShape(10.dp))
                    .padding(end = 14.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = "Delete hint",
                    tint = ListUi.swipeDeleteIcon,
                    modifier = Modifier.graphicsLayer {
                        alpha = deleteIconAlpha
                        scaleX = deleteIconScale
                        scaleY = deleteIconScale
                    }
                )
            }
        } else if (showEditHint) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(ListUi.swipeEditBg, RoundedCornerShape(10.dp))
                    .padding(start = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit hint",
                    tint = ListUi.swipeEditIcon,
                    modifier = Modifier.graphicsLayer {
                        alpha = editIconAlpha
                        scaleX = editIconScale
                        scaleY = editIconScale
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = ListUi.swipeRowMinHeight)
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(entry.id) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            offsetX = (offsetX + dragAmount).coerceIn(-maxOffsetPx, maxOffsetPx)
                        },
                        onDragEnd = {
                            when {
                                offsetX <= -commitThresholdPx -> onDelete()
                                offsetX >= commitThresholdPx -> onEdit()
                            }
                            offsetX = 0f
                        },
                        onDragCancel = { offsetX = 0f }
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("\u2022", color = DotColor, fontSize = ListUi.bulletSize)
                Spacer(Modifier.width(ListUi.bulletToText))
                Text(
                    text = entry.note,
                    color = PrimaryText,
                    fontFamily = AppFonts.rowLabel,
                    fontSize = ListUi.noteSize,
                )
            }
            Text(
                text = "-${formatCurrency(entry.amountMinor, currency)}",
                color = PrimaryText,
                fontFamily = AppFonts.rowValue,
                fontSize = ListUi.amountSize,
            )
        }
    }
}

@Composable
private fun EntryRow(entry: ExpenseEntry, currency: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ListUi.entryRowVerticalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("\u2022", color = DotColor, fontSize = ListUi.bulletSize)
            Spacer(Modifier.width(ListUi.bulletToText))
            Text(
                text = entry.note,
                color = PrimaryText,
                fontFamily = AppFonts.rowLabel,
                fontSize = ListUi.noteSize,
            )
        }
        Text(
            text = "-${formatCurrency(entry.amountMinor, currency)}",
            color = PrimaryText,
            fontFamily = AppFonts.rowValue,
            fontSize = ListUi.amountSize,
        )
    }
}

private fun quickInputFromEntry(entry: ExpenseEntry): String {
    val major = entry.amountMinor / 100.0
    return String.format(Locale.US, "%.2f %s", major, entry.note)
}

@Composable
private fun BudgetRow(b: BudgetProgress, currency: String) {
    val percent = if (b.limitMinor <= 0) 0 else ((b.spentMinor.toDouble() / b.limitMinor.toDouble()) * 100.0).toInt()
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = ListUi.budgetRowVerticalPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(b.label, color = PrimaryText, fontFamily = AppFonts.rowLabel)
            Text("${formatCurrency(b.spentMinor, currency)} / ${formatCurrency(b.limitMinor, currency)}", color = MutedText, fontSize = ListUi.budgetDetailSize)
        }
        Text("${percent}%", color = if (percent > 100) Color(0xFFFF7A7A) else Color(0xFF8F95AB), fontFamily = AppFonts.rowValue)
    }
}

@Composable
private fun BottomNav(current: Tab, onSelect: (Tab) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = NavUi.bottomPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(NavUi.dividerHeight)
                .background(NavDivider)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = NavUi.rowTopPadding, bottom = NavUi.rowBottomPadding),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavItem("Khatas", Icons.Outlined.Layers, current == Tab.KHATAS, Modifier.weight(1f)) { onSelect(Tab.KHATAS) }
            NavItem("Calendar", Icons.Outlined.CalendarMonth, current == Tab.CALENDAR, Modifier.weight(1f)) { onSelect(Tab.CALENDAR) }
            NavItem("Insights", Icons.Outlined.Insights, current == Tab.SHARE, Modifier.weight(1f)) { onSelect(Tab.SHARE) }
            NavItem("Settings", Icons.Outlined.Menu, current == Tab.SETTINGS, Modifier.weight(1f)) { onSelect(Tab.SETTINGS) }
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val tint = if (active) NavTintActive else NavTintInactive
    Column(
        modifier = modifier
            .padding(vertical = NavUi.itemVerticalPadding)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.width(NavUi.iconSize),
        )
        Spacer(Modifier.height(NavUi.labelTopSpacing))
        Text(
            text = label,
            color = tint,
            fontFamily = AppFonts.nav,
            fontSize = NavUi.labelSize,
            letterSpacing = NavUi.labelLetterSpacing,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun RoundArrowButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val bg = if (isPressed) CalendarUi.monthNavButtonPressedBg else CalendarUi.monthNavButtonBg

    Box(
        modifier = Modifier
            .size(CalendarUi.monthNavButtonSize)
            .background(bg, CircleShape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = PrimaryText,
            modifier = Modifier.size(CalendarUi.monthNavIconSize)
        )
    }
}

@Composable
private fun QuickEntryInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderSize: androidx.compose.ui.unit.TextUnit,
    inputSize: androidx.compose.ui.unit.TextUnit,
    hint: String? = null,
    onHintClick: (() -> Unit)? = null,
    onDone: () -> Unit,
) {
    val basePlaceholder = "Enter an amount"
    var placeholderText by remember { mutableStateOf(basePlaceholder) }
    var placeholderAnimationDone by remember { mutableStateOf(false) }

    LaunchedEffect(value.isEmpty(), placeholderAnimationDone) {
        if (!value.isEmpty()) return@LaunchedEffect
        if (placeholderAnimationDone) {
            placeholderText = basePlaceholder
            return@LaunchedEffect
        }

        placeholderText = basePlaceholder
        delay(KhatasUi.placeholderStartDelayMs)

        for (sample in KhatasUi.placeholderSamples) {
            for (idx in 1..sample.length) {
                if (!value.isEmpty()) return@LaunchedEffect
                placeholderText = sample.substring(0, idx)
                delay(KhatasUi.placeholderTypeMs)
            }
            delay(KhatasUi.placeholderHoldMs)
            for (idx in sample.length downTo 0) {
                if (!value.isEmpty()) return@LaunchedEffect
                placeholderText = sample.substring(0, idx)
                delay(KhatasUi.placeholderDeleteMs)
            }
            delay(KhatasUi.placeholderGapMs)
        }

        placeholderText = basePlaceholder
        placeholderAnimationDone = true
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(
            color = if (value.isBlank()) MutedText else PrimaryText,
            fontFamily = AppFonts.input,
            fontWeight = FontWeight.Normal,
            fontSize = inputSize,
            letterSpacing = 0.sp,
        ),
        singleLine = true,
        cursorBrush = SolidColor(PrimaryText),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            imeAction = ImeAction.Done,
            autoCorrectEnabled = false
        ),
        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
            onDone = { onDone() }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholderText,
                            color = MutedText,
                            fontSize = placeholderSize,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                    innerTextField()
                }
                if (!hint.isNullOrBlank() && onHintClick != null && value.isNotBlank()) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = hint,
                        color = AliasHintText,
                        fontFamily = AppFonts.helper,
                        fontSize = KhatasUi.hintTextSize,
                        modifier = Modifier
                            .background(AliasHintBg, RoundedCornerShape(KhatasUi.hintCorner))
                            .border(1.dp, AliasHintBorder, RoundedCornerShape(KhatasUi.hintCorner))
                            .clickable { onHintClick() }
                            .padding(
                                horizontal = KhatasUi.hintHorizontalPadding,
                                vertical = KhatasUi.hintVerticalPadding
                            )
                    )
                }
            }
        }
    )
}

private fun formatCurrency(amountMinor: Long, code: String): String {
    val major = amountMinor / 100
    val minor = kotlin.math.abs(amountMinor % 100)
    val symbol = when (code.uppercase()) {
        "INR" -> "\u20B9"
        "USD" -> "$"
        "EUR" -> "\u20AC"
        else -> ""
    }
    return "$symbol${major}.${minor.toString().padStart(2, '0')}"
}

private fun currencySymbol(code: String): String {
    return when (code.uppercase()) {
        "INR" -> "\u20B9"
        "USD" -> "$"
        "EUR" -> "\u20AC"
        else -> code.uppercase()
    }
}

private fun normalizeRange(a: LocalDate, b: LocalDate): Pair<LocalDate, LocalDate> {
    return if (a <= b) a to b else b to a
}

private fun monthDateAtOffset(
    x: Float,
    y: Float,
    monthCells: List<LocalDate?>,
    cellPx: Float,
    gapPx: Float,
    rows: Int,
    rowStep: Float,
): LocalDate? {
    if (x < 0f || y < 0f) return null
    val col = (x / rowStep).toInt()
    val row = (y / rowStep).toInt()
    if (col !in 0..6 || row !in 0 until rows) return null

    val xInCell = x - (col * rowStep)
    val yInCell = y - (row * rowStep)
    if (xInCell > cellPx || yInCell > cellPx) return null

    val idx = row * 7 + col
    if (idx !in monthCells.indices) return null
    return monthCells[idx]
}

private fun parseCreatedAtDate(createdAt: String): LocalDate {
    return try {
        Instant.parse(createdAt).atZone(ZoneId.systemDefault()).toLocalDate()
    } catch (_: Throwable) {
        LocalDate.now()
    }
}

private fun buildMonthCells(month: YearMonth): List<LocalDate?> {
    val first = month.atDay(1)
    val leading = first.dayOfWeek.value - 1
    val days = month.lengthOfMonth()
    val cells = mutableListOf<LocalDate?>()
    repeat(leading) { cells.add(null) }
    for (d in 1..days) cells.add(month.atDay(d))
    val trailing = (7 - (cells.size % 7)) % 7
    repeat(trailing) { cells.add(null) }
    return cells
}

