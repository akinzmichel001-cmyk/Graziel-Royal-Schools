package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ElegantDarkColorScheme = darkColorScheme(
    primary = Indigo500,
    onPrimary = Color.White,
    primaryContainer = Indigo900,
    onPrimaryContainer = Indigo400,
    secondary = Amber500,
    onSecondary = DarkCanvas,
    secondaryContainer = Color(0x33F59E0B),
    onSecondaryContainer = Amber400,
    tertiary = Emerald400,
    onTertiary = DarkCanvas,
    background = DarkCanvas,
    onBackground = Slate100,
    surface = DarkCardSurface,
    onSurface = Slate100,
    surfaceVariant = DarkCardSurfaceElevated,
    onSurfaceVariant = Slate400,
    outline = DarkBorder,
    outlineVariant = DarkBorderSubtle
)

@Composable
fun GrazielRoyalTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = ElegantDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkCanvas.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    GrazielRoyalTheme(darkTheme = darkTheme, content = content)
}
