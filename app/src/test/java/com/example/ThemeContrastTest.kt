package com.example

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.contentColorFor
import com.example.ui.theme.*
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.pow

class ThemeContrastTest {
    @Test fun allEightSchemesHaveReadableTextAndControls() {
        ColorPalette.entries.forEach { palette ->
            listOf(false, true).forEach { dark ->
                val c = paletteColorScheme(palette, dark)
                val name = "$palette ${if (dark) "dark" else "light"}"
                assertEquals("$name main card text", c.onSurface, c.contentColorFor(c.surfaceContainerHighest))
                val textPairs = listOf(
                    c.onPrimary to c.primary, c.onPrimaryContainer to c.primaryContainer,
                    c.onSecondary to c.secondary, c.onSecondaryContainer to c.secondaryContainer,
                    c.onTertiary to c.tertiary, c.onTertiaryContainer to c.tertiaryContainer,
                    c.onError to c.error, c.onErrorContainer to c.errorContainer,
                    c.inverseOnSurface to c.inverseSurface, c.inversePrimary to c.inverseSurface
                )
                textPairs.forEach { (text, background) -> assertContrast(name, text, background, 4.5) }
                val surfaces = listOf(c.background, c.surface, c.surfaceVariant, c.surfaceDim, c.surfaceBright,
                    c.surfaceContainerLowest, c.surfaceContainerLow, c.surfaceContainer, c.surfaceContainerHigh, c.surfaceContainerHighest)
                surfaces.forEach { background ->
                    listOf(c.onSurface, c.onSurfaceVariant, c.primary).forEach { text -> assertContrast(name, text, background, 4.5) }
                    assertContrast(name, c.outline, background, 3.0)
                }
                // Header and splash gradients use these endpoints; text must work over all of them.
                listOf(c.primaryContainer, c.secondaryContainer, c.tertiaryContainer).forEach { background ->
                    assertContrast(name, c.onSurface, background, 4.5)
                    assertContrast(name, c.onSurfaceVariant, background, 4.5)
                }
                assertNotEquals(c.error, c.primary)
                assertNotEquals(c.error, c.secondary)
                assertNotEquals(c.error, c.tertiary)
            }
        }
    }

    @Test fun systemModeKeepsPaletteWhileFollowingDeviceAppearance() {
        ColorPalette.entries.forEach { palette ->
            assertFalse(AppearanceMode.Light.isDark(true))
            assertTrue(AppearanceMode.Dark.isDark(false))
            assertFalse(AppearanceMode.System.isDark(false))
            assertTrue(AppearanceMode.System.isDark(true))
            assertNotEquals(paletteColorScheme(palette, false).background, paletteColorScheme(palette, true).background)
        }
    }

    private fun assertContrast(name: String, foreground: Color, background: Color, minimum: Double) {
        val a = luminance(foreground)
        val b = luminance(background)
        val ratio = (maxOf(a, b) + 0.05) / (minOf(a, b) + 0.05)
        assertTrue("$name: $foreground on $background has contrast $ratio, requires $minimum", ratio >= minimum)
    }

    private fun luminance(color: Color): Double {
        fun linear(value: Float): Double = if (value <= 0.04045f) value / 12.92 else ((value + 0.055) / 1.055).pow(2.4)
        return 0.2126 * linear(color.red) + 0.7152 * linear(color.green) + 0.0722 * linear(color.blue)
    }
}
