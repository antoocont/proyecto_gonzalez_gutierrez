package com.panalsuite.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Navy,
    onPrimary = Color.White,
    secondary = Teal,
    onSecondary = Color.White,
    tertiary = Amber,
    onTertiary = Navy,
    background = Cloud,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = CloudDark,
    onSurfaceVariant = Slate,
    outline = SlateLight
)

private val DarkColorScheme = darkColorScheme(
    primary = Amber,
    onPrimary = Navy,
    secondary = TealLight,
    onSecondary = Navy,
    tertiary = Amber,
    onTertiary = Navy,
    background = NightBackground,
    onBackground = NightText,
    surface = NightSurface,
    onSurface = NightText,
    surfaceVariant = NightVariant,
    onSurfaceVariant = NightMuted,
    outline = NightOutline
)

@Composable
fun PanalSuiteMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}