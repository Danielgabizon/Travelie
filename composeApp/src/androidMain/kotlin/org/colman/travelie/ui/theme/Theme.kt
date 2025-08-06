package org.colman.travelie.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Beige,           // primary  color (top bars, active icons)
    onPrimary = White,        // text/icon color on primary

    secondary = Terracotta,   // for primary buttons
    onSecondary = White,      // text/icon color on secondary

    tertiary = Beige,         // used for disabled states or alternate surfaces
    onTertiary = Navy,        // text/icon color on tertiary

    background = LightGray,   // app background color
    surface = White,          // card and container background

    onBackground = Navy,      // text/icon color on background
    onSurface = Navy          // text/icon color on surface (cards)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}