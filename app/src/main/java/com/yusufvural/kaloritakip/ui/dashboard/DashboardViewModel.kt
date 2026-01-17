package com.yusufvural.kaloritakip.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufvural.kaloritakip.model.DailySummary
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.MealType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.yusufvural.kaloritakip.domain.FoodRepository

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeFoodEntries()
    }

    private fun observeFoodEntries() {
        viewModelScope.launch {
            repository.getAllEntries().collect { entries ->
                _uiState.update { currentState ->
                    val totalCalories = entries.sumOf { it.calories }
                    val totalProtein = entries.sumOf { it.protein }
                    val totalCarbs = entries.sumOf { it.carbs }
                    val totalFat = entries.sumOf { it.fat }
                    
                    currentState.copy(
                        entries = entries,
                        summary = currentState.summary.copy(
                            totalCalories = totalCalories,
                            proteinConsumed = totalProtein,
                            carbsConsumed = totalCarbs,
                            fatConsumed = totalFat
                        )
                    )
                }
            }
        }
    }

    fun addFood(name: String, calories: Int, protein: Double, carbs: Double, fat: Double, mealType: MealType) {
        viewModelScope.launch {
            val newEntry = FoodEntry(
                id = UUID.randomUUID().toString(),
                name = name,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                mealType = mealType
            )
            repository.addFoodEntry(newEntry)
        }
    }

    fun addWater(amount: Int) {
        _uiState.update { it.copy(waterIntake = it.waterIntake + amount) }
    }
}

data class DashboardUiState(
    val summary: DailySummary = DailySummary(),
    val entries: List<FoodEntry> = emptyList(),
    val waterIntake: Int = 0,
    val waterGoal: Int = 2000,
    val steps: Int = 0,
    val stepGoal: Int = 10000,
    val isLoading: Boolean = false
)
