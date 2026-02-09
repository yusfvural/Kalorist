package com.yusufvural.kaloritakip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.Index

@Entity(
    tableName = "food_entries",
    indices = [Index(value = ["userId", "timestamp"])]
)
data class FoodEntry(
    @PrimaryKey val id: String,
    val name: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val mealType: MealType,
    val userId: String,
    val timestamp: Long = System.currentTimeMillis()
)
