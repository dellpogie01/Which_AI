package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    palette: ColorPalette = ColorPalette.Forest,
    content: @Composable () -> Unit,
) {
    // Deliberately independent of wallpaper colors on every Android version.
    val colors = paletteColorScheme(palette, darkTheme)
    MaterialTheme(colorScheme = colors, typography = Typography) {
        CompositionLocalProvider(LocalContentColor provides colors.onBackground, content = content)
    }
}
