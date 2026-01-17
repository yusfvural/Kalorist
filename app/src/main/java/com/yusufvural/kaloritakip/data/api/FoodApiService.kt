package com.yusufvural.kaloritakip.data.api

import retrofit2.http.GET
import retrofit2.http.Query

// Open Food Facts API Models
data class FoodSearchResponse(
    val products: List<Product> = emptyList()
)

data class Product(
    val product_name: String? = null,
    val nutriments: Nutriments? = null
)

data class Nutriments(
    val `energy-kcal_100g`: Double? = 0.0,
    val proteins_100g: Double? = 0.0,
    val fat_100g: Double? = 0.0,
    val carbohydrates_100g: Double? = 0.0
)

interface FoodApiService {
    @GET("cgi/search.pl")
    suspend fun searchFood(
        @Query("search_terms") query: String,
        @Query("lc") language: String = "tr,en", // Prioritize Turkish and English
        @Query("search_simple") simple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1
    ): FoodSearchResponse
}
