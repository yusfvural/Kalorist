package com.yusufvural.kaloritakip.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufvural.kaloritakip.domain.FoodRepository
import com.yusufvural.kaloritakip.model.FoodEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val userRepository: com.yusufvural.kaloritakip.domain.UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            // Combine food entries and user flow to get dynamic goals
            kotlinx.coroutines.flow.combine(
                repository.getAllEntries(),
                userRepository.getUser()
            ) { allEntries, user ->
                Pair(allEntries, user)
            }.collect { (allEntries, user) ->
                calculateWeeklyStats(allEntries, user?.dailyCalories ?: 2200)
            }
        }
    }

    private fun calculateWeeklyStats(allEntries: List<FoodEntry>, userCalorieGoal: Int) {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        val sevenDaysAgo = now - (7 * 24 * 60 * 60 * 1000L)

        val last7DaysEntries = allEntries.filter { it.timestamp >= sevenDaysAgo }

        // Günlük kalori toplamları
        val dailyCalories = mutableListOf<Int>()
        val dailyNames = mutableListOf<String>()
        val dayValues = mutableListOf<Float>()

        val tempCalendar = Calendar.getInstance()
        val daysLabels = listOf("Paz", "Pzt", "Sal", "Çar", "Per", "Cum", "Cmt")

        // Pazartesi'den başla
        tempCalendar.timeInMillis = now
        tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        if (tempCalendar.timeInMillis > now) {
            tempCalendar.add(Calendar.WEEK_OF_YEAR, -1)
        }
        
        val startOfMonday = tempCalendar.timeInMillis

        for (i in 0..6) {
            tempCalendar.timeInMillis = startOfMonday
            tempCalendar.add(Calendar.DAY_OF_YEAR, i)
            val dayOfYear = tempCalendar.get(Calendar.DAY_OF_YEAR)
            val dayName = daysLabels[tempCalendar.get(Calendar.DAY_OF_WEEK) - 1]
            
            val dayCalories = allEntries.filter { entry ->
                val entryCal = Calendar.getInstance()
                entryCal.timeInMillis = entry.timestamp
                entryCal.get(Calendar.DAY_OF_YEAR) == dayOfYear
            }.sumOf { it.calories }

            dailyCalories.add(dayCalories)
            dailyNames.add(dayName)
        }

        // Use userCalorieGoal instead of hardcoded 2200 for max scaling logic if reasonable
        // Usually chart max is driven by data max, but we can respect goal too
        val maxData = dailyCalories.maxOrNull()?.coerceAtLeast(1) ?: userCalorieGoal
        val chartMax = maxData.coerceAtLeast(userCalorieGoal) // Scale to at least the goal

        dailyCalories.forEach { cal ->
            dayValues.add(cal.toFloat() / chartMax.toFloat())
        }

        // Makro Dengesi
        val totalP = last7DaysEntries.sumOf { it.protein }
        val totalC = last7DaysEntries.sumOf { it.carbs }
        val totalF = last7DaysEntries.sumOf { it.fat }
        val totalMacros = (totalP + totalC + totalF).coerceAtLeast(1.0)

        // İçgörü
        val avgCal = dailyCalories.average().roundToInt()
        // Compare with userCalorieGoal
        val insightTitle = if (avgCal <= userCalorieGoal) "Harika Gidiyorsun!" else "Denge Zamanı"
        val insightMessage = if (avgCal <= userCalorieGoal) "Haftalık ortalaman hedeflerine uygun (${userCalorieGoal} kcal)." else "Haftalık ortalaman hedefinin (${userCalorieGoal} kcal) üzerinde."

        // Seri (Streak) Hesapla
        val streak = calculateStreak(allEntries)

        _uiState.update { 
            it.copy(
                averageCalories = avgCal,
                weeklyData = dayValues,
                weeklyLabels = dailyNames,
                weeklyCalories = dailyCalories,
                proteinPercentage = ((totalP / totalMacros) * 100).roundToInt(),
                carbsPercentage = ((totalC / totalMacros) * 100).roundToInt(),
                fatPercentage = ((totalF / totalMacros) * 100).roundToInt(),
                proteinGram = totalP.roundToInt(),
                carbsGram = totalC.roundToInt(),
                fatGram = totalF.roundToInt(),
                streakCount = streak,
                insightTitle = "${streak} Günlük Seri!",
                insightMessage = if (streak > 0) "Harika gidiyorsun, bu ateşi söndürme!" else "Bugün ilk adımını at ve serini başlat!"
            )
        }
    }

    private fun calculateStreak(entries: List<FoodEntry>): Int {
        if (entries.isEmpty()) return 0
        val sortedDates = entries.map { 
            val cal = Calendar.getInstance()
            cal.timeInMillis = it.timestamp
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }.distinct().sortedDescending()

        var streak = 0
        val currentCal = Calendar.getInstance()
        currentCal.set(Calendar.HOUR_OF_DAY, 0)
        currentCal.set(Calendar.MINUTE, 0)
        currentCal.set(Calendar.SECOND, 0)
        currentCal.set(Calendar.MILLISECOND, 0)
        
        var expectedDate = currentCal.timeInMillis

        // Bugün giriş varsa bugünden, yoksa dünden başla
        if (sortedDates.isEmpty() || sortedDates[0] < expectedDate) {
            // Eğer bugün giriş yoksa seri bozulmuş olabilir, dünü kontrol et
            val yesterday = expectedDate - 24 * 60 * 60 * 1000L
            if (sortedDates.isNotEmpty() && sortedDates[0] == yesterday) {
                expectedDate = yesterday
            } else {
                return 0 // Hiç giriş yok veya dünden önce kalmış
            }
        }

        for (date in sortedDates) {
            if (date == expectedDate) {
                streak++
                expectedDate -= 24 * 60 * 60 * 1000L
            } else if (date < expectedDate) {
                break
            }
        }
        return streak
    }
}

data class StatsUiState(
    val averageCalories: Int = 0,
    val weeklyData: List<Float> = emptyList(),
    val weeklyLabels: List<String> = emptyList(),
    val weeklyCalories: List<Int> = emptyList(),
    val proteinPercentage: Int = 0,
    val carbsPercentage: Int = 0,
    val fatPercentage: Int = 0,
    val proteinGram: Int = 0,
    val carbsGram: Int = 0,
    val fatGram: Int = 0,
    val streakCount: Int = 0,
    val insightTitle: String = "Analiz Ediliyor...",
    val insightMessage: String = "Verilerin hesaplanırken lütfen bekle."
)
