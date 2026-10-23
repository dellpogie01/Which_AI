package com.example

import com.example.ui.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class QuizRecommendationTest {
    @Test
    fun everyDisplayedGoalRecommendsTheMatchingTool() {
        val goals = mapOf(
            "Generate stunning Images & Art" to "midjourney",
            "Write emails, essays, and stories" to "chatgpt",
            "Research facts and find papers" to "perplexity",
            "Compose music and songs" to "suno_ai",
            "Produce videos and animations" to "runway_gen3",
            "Write code and build software" to "github_copilot"
        )
        goals.forEach { (goal, expectedId) ->
            val viewModel = MainViewModel()
            viewModel.startQuiz()
            viewModel.answerQuizGoal(goal)
            viewModel.answerQuizSkill("Absolute Beginner (Simple chat or taps)")
            viewModel.answerQuizBudget("Free tier only")
            assertEquals(goal, expectedId, viewModel.quizState.value.recommendedModel?.id)
            assertEquals(4, viewModel.quizState.value.step)
        }
    }
}
