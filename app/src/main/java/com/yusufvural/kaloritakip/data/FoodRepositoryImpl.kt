package com.yusufvural.kaloritakip.data

import com.yusufvural.kaloritakip.data.api.FoodApiService
import com.yusufvural.kaloritakip.domain.model.SearchResult
import com.yusufvural.kaloritakip.domain.FoodRepository
import com.yusufvural.kaloritakip.model.FoodEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
    private val apiService: FoodApiService
) : FoodRepository {

    override fun getAllEntries(): Flow<List<FoodEntry>> = foodDao.getAllEntries()

    override fun getEntriesForDay(timestamp: Long): Flow<List<FoodEntry>> = 
        foodDao.getEntriesForDay(timestamp)

    override suspend fun addFoodEntry(entry: FoodEntry) {
        foodDao.insertEntry(entry)
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
}
