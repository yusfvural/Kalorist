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
import java.util.*

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Mock initial data to match reference image
        loadInitialData()
    }

    private fun loadInitialData() {
        val mockEntries = listOf(
            FoodEntry(UUID.randomUUID().toString(), "Chicken Breast", 165, 31.0, 0.0, 3.6, MealType.LUNCH),
            FoodEntry(UUID.randomUUID().toString(), "Broccoli", 55, 3.7, 11.2, 0.6, MealType.LUNCH)
        )
        
        _uiState.update { currentState ->
            currentState.copy(
                entries = mockEntries,
                summary = DailySummary(
                    totalCalories = 1800,
                    goalCalories = 2200,
                    proteinConsumed = 85.0,
                    proteinGoal = 150.0,
                    carbsConsumed = 49.0,
                    carbsGoal = 68.0,
                    fatConsumed = 25.0,
                    fatGoal = 50.0
                ),
                steps = 6420,
                stepGoal = 10000
            )
        }
    }

    fun addFood(name: String, calories: Int, mealType: MealType) {
        val newEntry = FoodEntry(
            id = UUID.randomUUID().toString(),
            name = name,
            calories = calories,
            protein = 0.0, // Default for now
            carbs = 0.0,
            fat = 0.0,
            mealType = mealType
        )
        
        _uiState.update { currentState ->
            val updatedEntries = currentState.entries + newEntry
            currentState.copy(
                entries = updatedEntries,
                summary = currentState.summary.copy(
                    totalCalories = currentState.summary.totalCalories + calories
                )
            )
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
