package com.yusufvural.kaloritakip.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufvural.kaloritakip.domain.FoodRepository
import com.yusufvural.kaloritakip.model.ExerciseEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _waterIntake = MutableStateFlow(0)
    val waterIntake: StateFlow<Int> = _waterIntake.asStateFlow()

    private val _exercises = MutableStateFlow<List<ExerciseEntry>>(emptyList())
    val exercises: StateFlow<List<ExerciseEntry>> = _exercises.asStateFlow()

    init {
        observeDailyData()
    }

    private fun observeDailyData() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val todayStart = calendar.timeInMillis

            launch {
                repository.getTotalWaterForDay(todayStart).collectLatest { total ->
                    _waterIntake.value = total ?: 0
                }
            }

            launch {
                repository.getExercisesForDay(todayStart).collectLatest { list ->
                    _exercises.value = list
                }
            }
        }
    }

    fun addWater(amount: Int) {
        viewModelScope.launch {
            repository.addWaterEntry(amount)
        }
    }

    fun removeWater(amount: Int) {
        if (_waterIntake.value >= amount) {
            viewModelScope.launch {
                 repository.addWaterEntry(-amount)
            }
        }
    }

    fun addExercise(name: String, calories: Int, duration: Int) {
        viewModelScope.launch {
            val entry = ExerciseEntry(
                id = java.util.UUID.randomUUID().toString(),
                name = name,
                caloriesBurnt = calories,
                durationMinutes = duration
            )
            repository.addExerciseEntry(entry)
        }
    }

    fun deleteExercise(entry: ExerciseEntry) {
        viewModelScope.launch {
            repository.deleteExerciseEntry(entry)
        }
    }
}
