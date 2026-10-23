package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import android.graphics.Bitmap
import android.graphics.Canvas
import java.io.File
import com.example.data.AiRepository
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.RuntimeEnvironment
import androidx.core.view.WindowCompat
import com.example.ui.theme.ColorPalette

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "w320dp-h800dp")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AppNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun splashThenBottomNavigationAndThemePreferences() {
        compose.mainClock.autoAdvance = false
        compose.onNodeWithTag("splash_screen").assertExists()
        savePreview("splash")
        compose.mainClock.advanceTimeBy(2_800)
        compose.onNodeWithTag("splash_screen").assertExists()
        compose.mainClock.advanceTimeBy(400)
        compose.mainClock.autoAdvance = true
        compose.waitUntil(10_000) { compose.onAllNodesWithTag("nav_home").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("All (${AiRepository.aiModels.size})").assertExists()
        val description = compose.onNodeWithText("Best for: ${AiRepository.aiModels.first().bestFor.first()}", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        val compare = compose.onNodeWithTag("compare_add_btn_midjourney").fetchSemanticsNode().boundsInRoot
        assertTrue("Compare button should be below the description", compare.top >= description.bottom)
        savePreview("home")
        compose.onNodeWithTag("compare_add_btn_midjourney").performClick()
        compose.onNodeWithTag("nav_compare").performClick()
        compose.onNodeWithTag("compare_column_midjourney").assertExists()
        compose.onNodeWithTag("nav_wizard").performClick()
        compose.onNodeWithTag("start_quiz_btn").assertExists()
        compose.onNodeWithTag("nav_saved").performClick()
        compose.onNodeWithText("No saved bookmarks yet").assertExists()
        compose.onNodeWithTag("nav_search").performClick()
        compose.onNodeWithTag("search_input").performTextInput("Photoroom")
        compose.onNodeWithTag("ai_card_photoroom").assertExists()
        compose.onNodeWithTag("search_input").performTextClearance()
        compose.onNodeWithTag("settings_btn").performClick()
        compose.onNodeWithText("About Which AI").assertExists()
        compose.onNodeWithTag("theme_dark").performClick()
        savePreview("settings-dark")
        compose.runOnIdle {
            assertEquals("Dark", compose.activity.getSharedPreferences("appearance", 0).getString("theme", null))
        }
        compose.onNodeWithTag("theme_light").performClick()
        compose.runOnIdle {
            assertEquals("Light", compose.activity.getSharedPreferences("appearance", 0).getString("theme", null))
        }
        ColorPalette.entries.forEach { palette ->
            compose.onNodeWithTag("palette_${palette.name.lowercase()}").performScrollTo().performClick()
            listOf("light", "dark").forEach { mode ->
                compose.onNodeWithTag("theme_$mode").performScrollTo().performClick()
                compose.runOnIdle {
                    assertEquals(palette.name, compose.activity.getSharedPreferences("appearance", 0).getString("palette", null))
                }
                savePreview("settings-${palette.name.lowercase()}-$mode")
                compose.onNodeWithTag("nav_home").performClick()
                savePreview("home-${palette.name.lowercase()}-$mode")
                compose.onNodeWithTag("settings_btn").performClick()
            }
        }
        compose.onNodeWithTag("palette_ocean").performScrollTo().performClick()
        compose.onNodeWithTag("theme_system").performScrollTo().performClick()
        compose.runOnIdle { RuntimeEnvironment.setQualifiers("+night") }
        compose.activityRule.scenario.recreate()
        compose.runOnIdle {
            val storage = compose.activity.getSharedPreferences("appearance", 0)
            assertEquals("System", storage.getString("theme", null))
            assertEquals("Ocean", storage.getString("palette", null))
            assertFalse(WindowCompat.getInsetsController(compose.activity.window, compose.activity.window.decorView).isAppearanceLightStatusBars)
        }
        compose.runOnIdle { RuntimeEnvironment.setQualifiers("+notnight") }
        compose.activityRule.scenario.recreate()
        compose.runOnIdle {
            assertEquals("Ocean", compose.activity.getSharedPreferences("appearance", 0).getString("palette", null))
            assertTrue(WindowCompat.getInsetsController(compose.activity.window, compose.activity.window.decorView).isAppearanceLightStatusBars)
        }
        compose.onNodeWithTag("nav_home").performClick()
        compose.onNodeWithTag("search_input").assertExists()
    }

    private fun savePreview(name: String) {
        compose.runOnIdle {
            val view = compose.activity.window.decorView
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            view.draw(Canvas(bitmap))
            val file = File("build/ui-previews/$name.png")
            file.parentFile?.mkdirs()
            file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
    }
}
