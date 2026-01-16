package com.yusufvural.kaloritakip.model

data class FoodEntry(
    val id: String,
    val name: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val mealType: MealType
)
