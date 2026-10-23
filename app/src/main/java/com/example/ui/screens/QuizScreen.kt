package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiModel
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: MainViewModel,
    onModelClick: (AiModel) -> Unit,
    onBack: () -> Unit
) {
    val quizState by viewModel.quizState.collectAsState()

    Scaffold(contentWindowInsets = WindowInsets(0, 0, 0, 0)) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (quizState.step) {
                0 -> {
                    Text("💡", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Find Your Perfect AI Tool",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Answer 3 quick questions and our wizard will recommend the ideal AI system for your needs.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.startQuiz() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_quiz_btn")
                    ) {
                        Text("Start Wizard")
                    }
                }
                1 -> {
                    Text(
                        text = "Question 1 of 3",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "What do you want to create or accomplish?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    val goals = listOf(
                        "Generate stunning Images & Art",
                        "Write emails, essays, and stories",
                        "Research facts and find papers",
                        "Compose music and songs",
                        "Produce videos and animations",
                        "Write code and build software"
                    )
                    goals.forEach { goal ->
                        OutlinedButton(
                            onClick = { viewModel.answerQuizGoal(goal) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("quiz_goal_option")
                        ) {
                            Text(goal)
                        }
                    }
                }
                2 -> {
                    Text(
                        text = "Question 2 of 3",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "What is your comfort level with technology?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    val levels = listOf(
                        "Absolute Beginner (Simple chat or taps)",
                        "Intermediate (Comfortable with web apps)",
                        "Advanced (Tech-savvy / Developer)"
                    )
                    levels.forEach { level ->
                        OutlinedButton(
                            onClick = { viewModel.answerQuizSkill(level) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("quiz_skill_option")
                        ) {
                            Text(level)
                        }
                    }
                }
                3 -> {
                    Text(
                        text = "Question 3 of 3",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "What is your budget preference?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    val budgets = listOf(
                        "Free tier only",
                        "Willing to pay for top quality",
                        "Open source / Self-hosted"
                    )
                    budgets.forEach { budget ->
                        OutlinedButton(
                            onClick = { viewModel.answerQuizBudget(budget) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("quiz_budget_option")
                        ) {
                            Text(budget)
                        }
                    }
                }
                4 -> {
                    quizState.recommendedModel?.let { model ->
                        Text("🎉", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your Recommended AI:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quiz_result_card"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = model.emoji, fontSize = 48.sp)
                                Text(
                                    text = model.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = model.whatItDoes,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { onModelClick(model) },
                                    modifier = Modifier.testTag("view_recommended_model_btn")
                                ) {
                                    Text("View Full Details")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedButton(
                            onClick = { viewModel.resetQuiz() },
                            modifier = Modifier.testTag("retake_quiz_btn")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Retake")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retake Quiz")
                        }
                    }
                }
            }
        }
    }
}
