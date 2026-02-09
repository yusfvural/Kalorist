package com.yusufvural.kaloritakip.data

import com.yusufvural.kaloritakip.data.api.FoodApiService
import com.yusufvural.kaloritakip.domain.model.SearchResult
import com.yusufvural.kaloritakip.domain.FoodRepository
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.ExerciseEntry
import com.yusufvural.kaloritakip.model.WaterEntry
import java.util.Calendar
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
    private val exerciseDao: ExerciseDao,
    private val waterDao: WaterDao,
    private val apiService: FoodApiService,
    private val auth: com.google.firebase.auth.FirebaseAuth
) : FoodRepository {

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    override fun getAllEntries(): Flow<List<FoodEntry>> {
        val uid = currentUserId ?: return kotlinx.coroutines.flow.emptyFlow()
        return foodDao.getAllEntries(uid)
    }

    override fun getEntriesForDay(timestamp: Long): Flow<List<FoodEntry>> {
        val uid = currentUserId ?: return kotlinx.coroutines.flow.emptyFlow()
        val startOfDay = timestamp
        val endOfDay = timestamp + 86400000 // 24 hours in millis
        return foodDao.getEntriesForDay(uid, startOfDay, endOfDay)
    }

    override suspend fun addFoodEntry(entry: FoodEntry) {
        val uid = currentUserId ?: return
        foodDao.insertEntry(entry.copy(userId = uid))
    }

    override suspend fun deleteFoodEntry(entry: FoodEntry) {
        foodDao.deleteEntry(entry)
    }

    override suspend fun searchFood(query: String): Result<List<SearchResult>> {
        return try {
            val response = apiService.searchFood(query)
            val results = response.products
                .filter { it.product_name != null && it.nutriments != null }
                .map { product ->
                    SearchResult(
                        label = product.product_name!!,
                        calories = product.nutriments!!.`energy-kcal_100g`?.roundToInt() ?: 0,
                        protein = product.nutriments!!.proteins_100g ?: 0.0,
                        fat = product.nutriments!!.fat_100g ?: 0.0,
                        carbs = product.nutriments!!.carbohydrates_100g ?: 0.0
                    )
                }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getExercisesForDay(timestamp: Long): Flow<List<ExerciseEntry>> {
        val uid = currentUserId ?: return kotlinx.coroutines.flow.emptyFlow()
        val startOfDay = timestamp
        val endOfDay = timestamp + 86400000 // 24 hours in millis
        return exerciseDao.getExercisesForDay(uid, startOfDay, endOfDay)
    }

    override suspend fun addExerciseEntry(entry: ExerciseEntry) {
        val uid = currentUserId ?: return
        exerciseDao.insertExercise(entry.copy(userId = uid))
    }

    override suspend fun deleteExerciseEntry(entry: ExerciseEntry) {
        exerciseDao.deleteExercise(entry)
    }

    override fun getTotalWaterForDay(timestamp: Long): Flow<Int?> {
        val uid = currentUserId ?: return kotlinx.coroutines.flow.flowOf(0)
        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
        val dayStart = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val dayEnd = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
        return waterDao.getTotalWaterForDay(uid, dayStart, dayEnd)
    }

    override suspend fun addWaterEntry(amount: Int) {
        val uid = currentUserId ?: return
        waterDao.insertWater(
            WaterEntry(amountMl = amount, userId = uid)
        )
    }
}
