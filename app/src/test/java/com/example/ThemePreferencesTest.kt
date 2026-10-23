package com.example

import android.content.Context
import android.content.res.Configuration
import androidx.compose.ui.graphics.toArgb
import androidx.test.core.app.ApplicationProvider
import com.example.ui.theme.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ThemePreferencesTest {
    @Test fun nativeLaunchThemesUseWhiteSplashBackgroundInEveryMode() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        ColorPalette.entries.forEach { palette ->
            listOf(false, true).forEach { phoneDark ->
                val configuration = Configuration(context.resources.configuration).apply {
                    uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
                        if (phoneDark) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
                }
                val configured = context.createConfigurationContext(configuration)
                AppearanceMode.entries.forEach { mode ->
                    val attributes = android.view.ContextThemeWrapper(configured, nativeLaunchTheme(palette, mode))
                        .obtainStyledAttributes(intArrayOf(android.R.attr.windowSplashScreenBackground, android.R.attr.windowLightStatusBar))
                    val dark = mode.isDark(phoneDark)
                    assertEquals("$palette $mode phoneDark=$phoneDark", android.graphics.Color.WHITE, attributes.getColor(0, 0))
                    assertTrue(attributes.getBoolean(1, false))
                    attributes.recycle()
                }
            }
        }
    }

    @Test fun preferencesAreIndependentAndRestoreAcrossInstances() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val storage = context.getSharedPreferences("theme-preferences-test", 0)
        storage.edit().clear().commit()
        assertEquals(AppearanceMode.System, ThemePreferences(storage).appearanceMode)
        assertEquals(ColorPalette.Forest, ThemePreferences(storage).colorPalette)
        val preferences = ThemePreferences(storage)
        preferences.setAppearanceMode(AppearanceMode.Dark)
        preferences.setColorPalette(ColorPalette.Ocean)
        assertEquals(AppearanceMode.Dark, ThemePreferences(storage).appearanceMode)
        assertEquals(ColorPalette.Ocean, ThemePreferences(storage).colorPalette)
        preferences.setAppearanceMode(AppearanceMode.System)
        assertEquals(ColorPalette.Ocean, ThemePreferences(storage).colorPalette)
        preferences.setColorPalette(ColorPalette.Earth)
        assertEquals(AppearanceMode.System, ThemePreferences(storage).appearanceMode)
        assertEquals(ColorPalette.Earth, ThemePreferences(storage).colorPalette)
    }

    @Test fun legacyModeIsPreservedAndInvalidValuesFallBackSafely() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val storage = context.getSharedPreferences("theme-legacy-test", 0)
        storage.edit().clear().putString("theme", "Light").commit()
        assertEquals(AppearanceMode.Light, ThemePreferences(storage).appearanceMode)
        assertEquals(ColorPalette.Forest, ThemePreferences(storage).colorPalette)
        storage.edit().putString("theme", "unknown").putString("palette", "unknown").commit()
        assertEquals(AppearanceMode.System, ThemePreferences(storage).appearanceMode)
        assertEquals(ColorPalette.Forest, ThemePreferences(storage).colorPalette)
    }
}
