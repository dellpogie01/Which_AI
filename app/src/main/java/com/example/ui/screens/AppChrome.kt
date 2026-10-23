package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.BuildConfig
import com.example.R
import com.example.data.AiRepository
import com.example.ui.theme.AppearanceMode
import com.example.ui.theme.ColorPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedAppBar(subtitle: String, canGoBack: Boolean, onBack: () -> Unit, onSettings: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "header gradient")
    val position by transition.animateFloat(0f, 1000f, infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Reverse), label = "gradient position")
    val colors = MaterialTheme.colorScheme
    Box(Modifier.background(Brush.linearGradient(
        listOf(colors.primaryContainer, colors.secondaryContainer, colors.tertiaryContainer, colors.primaryContainer),
        start = Offset(position - 1000f, 0f), end = Offset(position + 1000f, 400f)))) {
        TopAppBar(
            title = { Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Which AI", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(subtitle, style = MaterialTheme.typography.labelMedium)
                }
            } },
            navigationIcon = { if (canGoBack) IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
            actions = { IconButton(onClick = onSettings, modifier = Modifier.testTag("settings_btn")) { Icon(Icons.Default.Settings, "Settings and app details") } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
    }
}

@Composable
fun SplashScreen() {
    val colors = MaterialTheme.colorScheme
    Box(Modifier.fillMaxSize().background(Color.White).testTag("splash_screen"), contentAlignment = Alignment.Center) {
        Column(Modifier.safeDrawingPadding().verticalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Image(painterResource(R.drawable.app_brand), "Which AI", Modifier.fillMaxWidth().aspectRatio(1f).testTag("splash_brand_image"))
            Text("Which AI", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Discover your next AI tool", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, color = colors.onSurfaceVariant)
            LinearProgressIndicator(modifier = Modifier.width(140.dp))
        }
    }
}

@Composable
fun SettingsScreen(mode: AppearanceMode, palette: ColorPalette, onModeChange: (AppearanceMode) -> Unit, onPaletteChange: (ColorPalette) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Appearance", style = MaterialTheme.typography.titleMedium)
        Text("Appearance mode", style = MaterialTheme.typography.titleSmall)
        Card(shape = RoundedCornerShape(20.dp)) {
            AppearanceMode.entries.forEach { choice ->
                Row(Modifier.fillMaxWidth().clickable { onModeChange(choice) }.padding(horizontal = 12.dp, vertical = 4.dp).testTag("theme_${choice.name.lowercase()}"), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = mode == choice, onClick = { onModeChange(choice) })
                    Column(Modifier.weight(1f)) {
                        Text(choice.label, style = MaterialTheme.typography.bodyLarge)
                        if (choice == AppearanceMode.System) Text("Follow your device appearance", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        Text("Color palette", style = MaterialTheme.typography.titleSmall)
        Card(shape = RoundedCornerShape(20.dp)) {
            ColorPalette.entries.forEach { choice ->
                Row(Modifier.fillMaxWidth().clickable { onPaletteChange(choice) }.padding(horizontal = 12.dp, vertical = 4.dp).testTag("palette_${choice.name.lowercase()}"), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = palette == choice, onClick = { onPaletteChange(choice) })
                    Text(choice.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        Text("About Which AI", style = MaterialTheme.typography.titleMedium)
        Card(shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Discover your next AI tool", style = MaterialTheme.typography.titleMedium)
                Text("Explore ${AiRepository.aiModels.size} AI tools and systems across ${AiRepository.categories.size - 1} categories. Search the catalog, compare two tools, get a guided recommendation, and save your favorites.")
                Text("Version ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.labelLarge)
                Text("The catalog is available offline. Official website links open in your browser. Features and availability may change; check each provider for current details.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Theme preferences are saved on this device. Bookmarks and comparison selections last for the current app session.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
