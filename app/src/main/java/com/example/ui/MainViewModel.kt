package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AiRepository
import com.example.model.AiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class QuizState(
    val step: Int = 0, // 0 = not started, 1 = question 1, 2 = question 2, 3 = question 3, 4 = result
    val goal: String = "",
    val skillLevel: String = "",
    val budget: String = "",
    val recommendedModel: AiModel? = null
)

class MainViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _bookmarkedIds = MutableStateFlow(setOf<String>())
    val bookmarkedIds: StateFlow<Set<String>> = _bookmarkedIds.asStateFlow()

    private val _compareList = MutableStateFlow<List<AiModel>>(emptyList())
    val compareList: StateFlow<List<AiModel>> = _compareList.asStateFlow()

    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    val filteredModels: StateFlow<List<AiModel>> = combine(
        _searchQuery,
        _selectedCategory
    ) { query, category ->
        AiRepository.aiModels.filter { model ->
            val matchesCategory = category == "All" || model.category == category
            val matchesQuery = query.isBlank() ||
                    model.name.contains(query, ignoreCase = true) ||
                    model.whatItDoes.contains(query, ignoreCase = true) ||
                    model.bestFor.any { it.contains(query, ignoreCase = true) }
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AiRepository.aiModels
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleBookmark(id: String) {
        val current = _bookmarkedIds.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _bookmarkedIds.value = current
    }

    fun addToCompare(model: AiModel) {
        val current = _compareList.value.toMutableList()
        if (!current.any { it.id == model.id }) {
            if (current.size >= 2) {
                current.removeAt(0)
            }
            current.add(model)
            _compareList.value = current
        }
    }

    fun removeFromCompare(id: String) {
        _compareList.value = _compareList.value.filter { it.id != id }
    }

    fun clearCompare() {
        _compareList.value = emptyList()
    }

    fun startQuiz() {
        _quizState.value = QuizState(step = 1)
    }

    fun answerQuizGoal(goal: String) {
        _quizState.value = _quizState.value.copy(goal = goal, step = 2)
    }

    fun answerQuizSkill(skill: String) {
        _quizState.value = _quizState.value.copy(skillLevel = skill, step = 3)
    }

    fun answerQuizBudget(budget: String) {
        val currentState = _quizState.value.copy(budget = budget, step = 4)
        // Determine recommendation based on answers
        val rec = when {
            currentState.goal.contains("Images", true) -> AiRepository.aiModels.find { it.id == "midjourney" }
            currentState.goal.startsWith("Write emails", true) -> AiRepository.aiModels.find { it.id == "chatgpt" }
            currentState.goal.contains("Research", true) -> AiRepository.aiModels.find { it.id == "perplexity" }
            currentState.goal.contains("Music", true) -> AiRepository.aiModels.find { it.id == "suno_ai" }
            currentState.goal.contains("Video", true) -> AiRepository.aiModels.find { it.id == "runway_gen3" }
            currentState.goal.startsWith("Write code", true) -> AiRepository.aiModels.find { it.id == "github_copilot" }
            else -> AiRepository.aiModels.find { it.id == "claude" }
        } ?: AiRepository.aiModels.first()

        _quizState.value = currentState.copy(recommendedModel = rec)
    }

    fun resetQuiz() {
        _quizState.value = QuizState()
    }
}
