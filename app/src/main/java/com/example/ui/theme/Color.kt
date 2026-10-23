package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

enum class ColorPalette { Forest, Ocean, Royal, Earth }

enum class AppearanceMode(val label: String) {
    Light("Light"), Dark("Dark"), System("System default");

    fun isDark(systemDark: Boolean): Boolean = when (this) {
        Light -> false
        Dark -> true
        System -> systemDark
    }
}

private data class Accent(
    val color: Color, val onColor: Color, val container: Color, val onContainer: Color
)

private fun accent(color: Long, onColor: Long, container: Long, onContainer: Long) =
    Accent(Color(color), Color(onColor), Color(container), Color(onContainer))

/**
 * All semantic roles, including surface elevation levels and inverse colors, live here.
 * Primary is the action color; secondary and tertiary are coordinated palette accents.
 * Forest uses gold actions, burgundy secondary accents, and forest-green neutrals.
 */
private fun scheme(
    dark: Boolean,
    primary: Accent, secondary: Accent, tertiary: Accent, inversePrimary: Long,
    background: Long, surface: Long, low: Long, container: Long, high: Long, highest: Long,
    onSurface: Long, onSurfaceVariant: Long, outline: Long, outlineVariant: Long
): ColorScheme {
    val base = if (dark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = primary.color, onPrimary = primary.onColor,
        primaryContainer = primary.container, onPrimaryContainer = primary.onContainer,
        secondary = secondary.color, onSecondary = secondary.onColor,
        secondaryContainer = secondary.container, onSecondaryContainer = secondary.onContainer,
        tertiary = tertiary.color, onTertiary = tertiary.onColor,
        tertiaryContainer = tertiary.container, onTertiaryContainer = tertiary.onContainer,
        background = Color(background), onBackground = Color(onSurface),
        surface = Color(surface), onSurface = Color(onSurface),
        // Keep this distinct from elevated surfaces so contentColorFor(cards) resolves to onSurface.
        surfaceVariant = lerp(Color(high), Color(highest), 0.5f), onSurfaceVariant = Color(onSurfaceVariant),
        surfaceTint = primary.color,
        surfaceDim = Color(if (dark) background else highest),
        surfaceBright = Color(if (dark) highest else surface),
        surfaceContainerLowest = Color(background),
        surfaceContainerLow = Color(low), surfaceContainer = Color(container),
        surfaceContainerHigh = Color(high), surfaceContainerHighest = Color(highest),
        outline = Color(outline), outlineVariant = Color(outlineVariant),
        inverseSurface = Color(if (dark) 0xFFECEBE5 else 0xFF2F302D),
        inverseOnSurface = Color(if (dark) 0xFF292B26 else 0xFFF4F3ED),
        inversePrimary = Color(inversePrimary),
        // Error red remains separate from burgundy, coral, and terracotta accents.
        error = Color(if (dark) 0xFFFFB4AB else 0xFFBA1A1A),
        onError = Color(if (dark) 0xFF690005 else 0xFFFFFFFF),
        errorContainer = Color(if (dark) 0xFF93000A else 0xFFFFDAD6),
        onErrorContainer = Color(if (dark) 0xFFFFDAD6 else 0xFF410002),
        scrim = Color(0xFF000000)
    )
}

private val ForestLight = scheme(
    dark = false,
    primary = accent(0xFF775B00, 0xFFFFFFFF, 0xFFF2E2AA, 0xFF2C2200),
    inversePrimary = 0xFFE7C66B,
    secondary = accent(0xFF792F47, 0xFFFFFFFF, 0xFFF8DCE3, 0xFF401322),
    tertiary = accent(0xFF245438, 0xFFFFFFFF, 0xFFCEE8D5, 0xFF102D1C),
    background = 0xFFF7FAF5,
    surface = 0xFFEFF5ED,
    low = 0xFFEAF1E7,
    container = 0xFFE3EBDD,
    high = 0xFFDCE6D6,
    highest = 0xFFD5E0CE,
    onSurface = 0xFF17251B,
    onSurfaceVariant = 0xFF435347,
    outline = 0xFF728174,
    outlineVariant = 0xFFB8C5B6
)

private val ForestDark = scheme(
    dark = true,
    primary = accent(0xFFE7C66B, 0xFF261D00, 0xFF44380F, 0xFFF5E3AA),
    inversePrimary = 0xFF775B00,
    secondary = accent(0xFFE6A3B3, 0xFF401322, 0xFF5C2638, 0xFFFFD9E3),
    tertiary = accent(0xFFA5D2AF, 0xFF103820, 0xFF294D34, 0xFFCEEBD4),
    background = 0xFF08120D,
    surface = 0xFF12261B,
    low = 0xFF152B1E,
    container = 0xFF193122,
    high = 0xFF1D3627,
    highest = 0xFF213C2C,
    onSurface = 0xFFF3F0E5,
    onSurfaceVariant = 0xFFC0CABC,
    outline = 0xFF819681,
    outlineVariant = 0xFF415746
)

private val OceanLight = scheme(
    dark = false,
    primary = accent(0xFF173D70, 0xFFFFFFFF, 0xFFD7E5FF, 0xFF0B2448),
    inversePrimary = 0xFFA9CBFF,
    secondary = accent(0xFF006665, 0xFFFFFFFF, 0xFFC3EEEB, 0xFF003736),
    tertiary = accent(0xFF944334, 0xFFFFFFFF, 0xFFFFDBD2, 0xFF4F1C12),
    background = 0xFFF7FAFE,
    surface = 0xFFF0F5FA,
    low = 0xFFEAF1F7,
    container = 0xFFE3EBF2,
    high = 0xFFDCE5ED,
    highest = 0xFFD5DFE8,
    onSurface = 0xFF182631,
    onSurfaceVariant = 0xFF455561,
    outline = 0xFF6F7F89,
    outlineVariant = 0xFFBAC7D0
)

private val OceanDark = scheme(
    dark = true,
    primary = accent(0xFFA9CBFF, 0xFF082A52, 0xFF223F66, 0xFFD7E5FF),
    inversePrimary = 0xFF173D70,
    secondary = accent(0xFF78D5CE, 0xFF003733, 0xFF10504C, 0xFFB5F2EB),
    tertiary = accent(0xFFFFB5A2, 0xFF562217, 0xFF74392C, 0xFFFFDBD2),
    background = 0xFF0A121E,
    surface = 0xFF121F31,
    low = 0xFF162438,
    container = 0xFF1A293E,
    high = 0xFF1F2F45,
    highest = 0xFF24354D,
    onSurface = 0xFFE8EFF8,
    onSurfaceVariant = 0xFFBBC9D8,
    outline = 0xFF8395A9,
    outlineVariant = 0xFF40526A
)

private val RoyalLight = scheme(
    dark = false,
    primary = accent(0xFF59328C, 0xFFFFFFFF, 0xFFEBDDFB, 0xFF2E104F),
    inversePrimary = 0xFFD5B5FA,
    secondary = accent(0xFF65518A, 0xFFFFFFFF, 0xFFEBDCFF, 0xFF301E50),
    tertiary = accent(0xFF745800, 0xFFFFFFFF, 0xFFF4E2AA, 0xFF332600),
    background = 0xFFFCF8FF,
    surface = 0xFFF5EFFA,
    low = 0xFFEFE9F5,
    container = 0xFFE9E2F0,
    high = 0xFFE3DCEB,
    highest = 0xFFDDD5E5,
    onSurface = 0xFF281C35,
    onSurfaceVariant = 0xFF594B65,
    outline = 0xFF82748B,
    outlineVariant = 0xFFCEC0D8
)

private val RoyalDark = scheme(
    dark = true,
    primary = accent(0xFFD5B5FA, 0xFF321253, 0xFF50316E, 0xFFEEDCFF),
    inversePrimary = 0xFF59328C,
    secondary = accent(0xFFCCB9EA, 0xFF33234D, 0xFF4A3864, 0xFFEBDCFF),
    tertiary = accent(0xFFE8C66F, 0xFF322500, 0xFF4E3D0A, 0xFFF7E4AC),
    background = 0xFF140E1D,
    surface = 0xFF24192F,
    low = 0xFF291E35,
    container = 0xFF2F233C,
    high = 0xFF352942,
    highest = 0xFF3C2F4A,
    onSurface = 0xFFF1EAF8,
    onSurfaceVariant = 0xFFCDBFD9,
    outline = 0xFFA28EAF,
    outlineVariant = 0xFF584463
)

private val EarthLight = scheme(
    dark = false,
    primary = accent(0xFF674330, 0xFFFFFFFF, 0xFFF4DECF, 0xFF351D10),
    inversePrimary = 0xFFE4BFA6,
    secondary = accent(0xFF495E3E, 0xFFFFFFFF, 0xFFDBE8CF, 0xFF203219),
    tertiary = accent(0xFF963F2B, 0xFFFFFFFF, 0xFFFFDBCE, 0xFF4C1C0E),
    background = 0xFFFFF9F3,
    surface = 0xFFF7F0E7,
    low = 0xFFF1EADD,
    container = 0xFFEBE3D6,
    high = 0xFFE5DDCF,
    highest = 0xFFDFD6C7,
    onSurface = 0xFF302219,
    onSurfaceVariant = 0xFF625347,
    outline = 0xFF847567,
    outlineVariant = 0xFFCFC1AF
)

private val EarthDark = scheme(
    dark = true,
    primary = accent(0xFFE4BFA6, 0xFF3B2416, 0xFF573B2A, 0xFFFFE0CA),
    inversePrimary = 0xFF674330,
    secondary = accent(0xFFB5CEA2, 0xFF23371A, 0xFF394D2E, 0xFFD7EBC5),
    tertiary = accent(0xFFF5AF92, 0xFF512416, 0xFF753C28, 0xFFFFDBCE),
    background = 0xFF19120E,
    surface = 0xFF2B2019,
    low = 0xFF30251D,
    container = 0xFF362A21,
    high = 0xFF3D3026,
    highest = 0xFF44362B,
    onSurface = 0xFFF6EDE1,
    onSurfaceVariant = 0xFFD2C2B2,
    outline = 0xFFAC9783,
    outlineVariant = 0xFF63503F
)

fun paletteColorScheme(palette: ColorPalette, dark: Boolean): ColorScheme = when (palette) {
    ColorPalette.Forest -> if (dark) ForestDark else ForestLight
    ColorPalette.Ocean -> if (dark) OceanDark else OceanLight
    ColorPalette.Royal -> if (dark) RoyalDark else RoyalLight
    ColorPalette.Earth -> if (dark) EarthDark else EarthLight
}
