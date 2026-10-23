package com.example

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.view.WindowCompat
import com.example.data.AiRepository
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ThemePreferences
import com.example.ui.theme.nativeLaunchTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferences = ThemePreferences(getSharedPreferences("appearance", MODE_PRIVATE))
        setContent {
            var mode by rememberSaveable { mutableStateOf(preferences.appearanceMode) }
            var palette by rememberSaveable { mutableStateOf(preferences.colorPalette) }
            val dark = mode.isDark(isSystemInDarkTheme())
            var splash by rememberSaveable { mutableStateOf(true) }
            MyApplicationTheme(darkTheme = dark, palette = palette) {
                val colors = MaterialTheme.colorScheme
                SideEffect {
                    window.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(if (splash) android.graphics.Color.WHITE else colors.background.toArgb()))
                    if (android.os.Build.VERSION.SDK_INT >= 31) {
                        splashScreen.setSplashScreenTheme(nativeLaunchTheme(palette, mode))
                    }
                    val lightBars = splash || !dark
                    val barStyle = if (!lightBars) SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                        else SystemBarStyle.light(android.graphics.Color.TRANSPARENT, colors.inverseSurface.toArgb())
                    enableEdgeToEdge(statusBarStyle = barStyle, navigationBarStyle = barStyle)
                    if (android.os.Build.VERSION.SDK_INT >= 29) window.isNavigationBarContrastEnforced = false
                    WindowCompat.getInsetsController(window, window.decorView).apply {
                        isAppearanceLightStatusBars = lightBars
                        isAppearanceLightNavigationBars = lightBars
                    }
                }
                val deadline = rememberSaveable { SystemClock.elapsedRealtime() + 3_000L }
                LaunchedEffect(Unit) {
                    delay((deadline - SystemClock.elapsedRealtime()).coerceAtLeast(0L))
                    splash = false
                }
                if (splash) {
                    MyApplicationTheme(darkTheme = false, palette = palette) { SplashScreen() }
                } else {
                    val model: MainViewModel = viewModel()
                    var destination by rememberSaveable { mutableStateOf("Home") }
                    var detailId by rememberSaveable { mutableStateOf<String?>(null) }
                    var settings by rememberSaveable { mutableStateOf(false) }
                    val back = { if (detailId != null) detailId = null else { settings = false; destination = "Home" } }
                    BackHandler(settings || detailId != null || destination != "Home", onBack = back)
                    val destinations = listOf("Home", "Search", "Compare", "Wizard", "Saved")
                    val icons = listOf(Icons.Default.Home, Icons.Default.Search, Icons.AutoMirrored.Filled.CompareArrows, Icons.Default.Lightbulb, Icons.Default.Bookmark)
                    Scaffold(
                        topBar = {
                            AnimatedAppBar(
                                subtitle = when {
                                    settings -> "Settings & app details"
                                    detailId != null -> "Explore this AI tool"
                                    destination == "Compare" -> "Compare AI models"
                                    destination == "Wizard" -> "AI recommendation wizard"
                                    destination == "Saved" -> "Saved AI bookmarks"
                                    else -> "Discover your next AI tool"
                                },
                                canGoBack = settings || detailId != null,
                                onBack = back,
                                onSettings = { settings = true; detailId = null }
                            )
                        },
                        bottomBar = {
                            NavigationBar {
                                destinations.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = destination == item && !settings,
                                        onClick = { destination = item; detailId = null; settings = false },
                                        icon = { Icon(icons[index], contentDescription = when(item) { "Compare" -> "Compare AI models"; "Wizard" -> "AI recommendation wizard"; "Saved" -> "Saved AI bookmarks"; else -> item }) },
                                        label = { Text(item, fontSize = 10.sp, letterSpacing = 0.sp, maxLines = 1, softWrap = false) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = colors.onPrimary,
                                            selectedTextColor = colors.primary,
                                            indicatorColor = colors.primary,
                                            unselectedIconColor = colors.onSurfaceVariant,
                                            unselectedTextColor = colors.onSurfaceVariant
                                        ),
                                        modifier = Modifier.testTag("nav_${item.lowercase()}")
                                    )
                                }
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.background
                    ) { padding ->
                        Box(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
                            val detail = AiRepository.aiModels.find { it.id == detailId }
                            when {
                                settings -> SettingsScreen(
                                    mode = mode, palette = palette,
                                    onModeChange = { mode = it; preferences.setAppearanceMode(it) },
                                    onPaletteChange = { palette = it; preferences.setColorPalette(it) }
                                )
                                detail != null -> DetailScreen(detail, model) { detailId = null }
                                destination == "Compare" -> CompareScreen(model) { destination = "Home" }
                                destination == "Wizard" -> QuizScreen(model, { detailId = it.id }) { destination = "Home" }
                                destination == "Saved" -> BookmarksScreen(model, { detailId = it.id }) { destination = "Home" }
                                else -> HomeScreen(model, { detailId = it.id }, searchMode = destination == "Search")
                            }
                        }
                    }
                }
            }
        }
    }
}
