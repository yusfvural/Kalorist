package com.yusufvural.kaloritakip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_entries")
data class FoodEntry(
    @PrimaryKey val id: String,
    val name: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val mealType: MealType,
    val timestamp: Long = System.currentTimeMillis()
)
