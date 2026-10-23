package com.example.ui.theme

import com.example.R

/** Stable names are persisted by Android for subsequent system launch screens. */
fun nativeLaunchTheme(palette: ColorPalette, mode: AppearanceMode): Int = when (palette) {
    ColorPalette.Forest -> when (mode) {
        AppearanceMode.Light -> R.style.Theme_WhichAI_Forest_Light
        AppearanceMode.Dark -> R.style.Theme_WhichAI_Forest_Dark
        AppearanceMode.System -> R.style.Theme_WhichAI_Forest_System
    }
    ColorPalette.Ocean -> when (mode) {
        AppearanceMode.Light -> R.style.Theme_WhichAI_Ocean_Light
        AppearanceMode.Dark -> R.style.Theme_WhichAI_Ocean_Dark
        AppearanceMode.System -> R.style.Theme_WhichAI_Ocean_System
    }
    ColorPalette.Royal -> when (mode) {
        AppearanceMode.Light -> R.style.Theme_WhichAI_Royal_Light
        AppearanceMode.Dark -> R.style.Theme_WhichAI_Royal_Dark
        AppearanceMode.System -> R.style.Theme_WhichAI_Royal_System
    }
    ColorPalette.Earth -> when (mode) {
        AppearanceMode.Light -> R.style.Theme_WhichAI_Earth_Light
        AppearanceMode.Dark -> R.style.Theme_WhichAI_Earth_Dark
        AppearanceMode.System -> R.style.Theme_WhichAI_Earth_System
    }
}
