package com.example.ui.theme

import android.content.SharedPreferences
import androidx.core.content.edit

/** Retains the original theme key so existing Light/Dark/System preferences are preserved. */
class ThemePreferences(private val preferences: SharedPreferences) {
    val appearanceMode: AppearanceMode
        get() = AppearanceMode.entries.find { it.name == preferences.getString("theme", null) } ?: AppearanceMode.System

    val colorPalette: ColorPalette
        get() = ColorPalette.entries.find { it.name == preferences.getString("palette", null) } ?: ColorPalette.Forest

    fun setAppearanceMode(mode: AppearanceMode) {
        preferences.edit { putString("theme", mode.name) }
    }

    fun setColorPalette(palette: ColorPalette) {
        preferences.edit { putString("palette", palette.name) }
    }
}
