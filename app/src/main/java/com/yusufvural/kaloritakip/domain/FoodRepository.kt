package com.yusufvural.kaloritakip.domain

import com.yusufvural.kaloritakip.domain.model.SearchResult
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.ExerciseEntry
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getAllEntries(): Flow<List<FoodEntry>>
    fun getEntriesForDay(timestamp: Long): Flow<List<FoodEntry>>
    suspend fun addFoodEntry(entry: FoodEntry)
    suspend fun deleteFoodEntry(entry: FoodEntry)
    suspend fun searchFood(query: String): Result<List<SearchResult>>

    // Exercise Methods
    fun getExercisesForDay(timestamp: Long): Flow<List<ExerciseEntry>>
    suspend fun addExerciseEntry(entry: ExerciseEntry)
    suspend fun deleteExerciseEntry(entry: ExerciseEntry)

    // Water Methods
    fun getTotalWaterForDay(timestamp: Long): Flow<Int?>
    suspend fun addWaterEntry(amount: Int)
}
