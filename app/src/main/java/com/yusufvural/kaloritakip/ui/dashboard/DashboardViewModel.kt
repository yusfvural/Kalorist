package com.yusufvural.kaloritakip.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufvural.kaloritakip.model.DailySummary
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.MealType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.DinnerDining
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.LunchDining
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.MutableStateFlow
import com.yusufvural.kaloritakip.R
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
    private val repository: FoodRepository,
    private val userRepository: com.yusufvural.kaloritakip.domain.UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeFoodEntries()
        calculateStreak()
    }

    private fun observeFoodEntries() {
        viewModelScope.launch {
            // Bugünün başlangıç timestamp'ini al
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfDay = calendar.timeInMillis

            // Combine all flows
            kotlinx.coroutines.flow.combine(
                repository.getEntriesForDay(startOfDay),
                repository.getExercisesForDay(startOfDay),
                userRepository.getUser()
            ) { foodEntries, exerciseEntries, user ->
                Triple(foodEntries, exerciseEntries, user)
            }.collect { (foodEntries, exerciseEntries, user) ->
                val totalCalories = foodEntries.sumOf { it.calories }
                val totalProtein = foodEntries.sumOf { it.protein }
                val totalCarbs = foodEntries.sumOf { it.carbs }
                val totalFat = foodEntries.sumOf { it.fat }
                
                val totalBurnt = exerciseEntries.sumOf { it.caloriesBurnt }

                // Use user goals if available, otherwise defaults
                val dailyGoal = user?.dailyCalories ?: 2200
                val proteinGoal = user?.proteinGoal ?: 150.0
                val carbGoal = user?.carbGoal ?: 300.0
                val fatGoal = user?.fatGoal ?: 70.0

                    val mealsConfig = listOf(
                        Triple(MealType.BREAKFAST, R.string.breakfast, Icons.Rounded.Coffee),
                        Triple(MealType.LUNCH, R.string.lunch, Icons.Rounded.LunchDining),
                        Triple(MealType.DINNER, R.string.dinner, Icons.Rounded.DinnerDining),
                        Triple(MealType.SNACK, R.string.snack, Icons.Rounded.Fastfood)
                    )

                    val mealList = mealsConfig.map { (type, labelResId, icon) ->
                        val mealEntries = foodEntries.filter { it.mealType == type }
                        val currentCal = mealEntries.sumOf { it.calories }
                        // Hardcoded goals for now, matches previous UI logic
                        val goal = when(type) {
                            MealType.BREAKFAST -> 566
                            MealType.LUNCH -> 755
                            MealType.DINNER -> 600
                            MealType.SNACK -> 200
                        }
                        
                        val description = if (mealEntries.isEmpty()) "" else mealEntries.joinToString(", ") { it.name }.take(35) + (if (mealEntries.joinToString(", ").length > 35) "..." else "")

                        MealUiModel(
                            labelResId = labelResId,
                            icon = icon,
                            type = type,
                            currentCal = currentCal,
                            goalCal = goal,
                            description = description,
                            isExceeded = currentCal > goal && goal > 0
                        )
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            entries = foodEntries,
                            summary = DailySummary(
                                totalCalories = totalCalories,
                                goalCalories = dailyGoal,
                                burnedCalories = totalBurnt,
                                proteinConsumed = totalProtein,
                                proteinGoal = proteinGoal,
                                carbsConsumed = totalCarbs,
                                carbsGoal = carbGoal,
                                fatConsumed = totalFat,
                                fatGoal = fatGoal
                            ),
                            mealList = mealList
                        )
                    }
            }
        }
    }

    private fun calculateStreak() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Pazartesi"
            Calendar.TUESDAY -> "Salı"
            Calendar.WEDNESDAY -> "Çarşamba"
            Calendar.THURSDAY -> "Perşembee"
            Calendar.FRIDAY -> "Cuma"
            Calendar.SATURDAY -> "Cumartesi"
            Calendar.SUNDAY -> "Pazar"
            else -> "Bugün"
        }
        
        _uiState.update { it.copy(
            dayName = dayOfWeek,
            streakCount = 5,
            points = 2000
        ) }
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
                mealType = mealType,
                userId = ""
            )
            repository.addFoodEntry(newEntry)
        }
    }

    fun deleteFood(entry: FoodEntry) {
        viewModelScope.launch {
            repository.deleteFoodEntry(entry)
        }
    }
}

data class DashboardUiState(
    val summary: DailySummary = DailySummary(),
    val entries: List<FoodEntry> = emptyList(),
    val steps: Int = 0,
    val stepGoal: Int = 10000,
    val streakCount: Int = 0,
    val points: Int = 0,
    val dayName: String = "Bugün",
    val weekCount: Int = 1,
    val isLoading: Boolean = false,
    val mealList: List<MealUiModel> = emptyList()
)

data class MealUiModel(
    val labelResId: Int,
    val icon: ImageVector,
    val type: MealType,
    val currentCal: Int,
    val goalCal: Int,
    val description: String,
    val isExceeded: Boolean
)
