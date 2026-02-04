package com.yusufvural.kaloritakip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val id: Int = 0, // Single user for now, always ID 0
    
    // Basic Info
    val name: String = "Kullanıcı",
    val age: Int = 25,
    val height: Int = 175, // cm
    val currentWeight: Double = 75.0, // kg
    val gender: String = "Erkek", // Erkek, Kadın
    val activityLevel: Float = 1.2f, // 1.2, 1.375, 1.55 etc.
    
    // Goals
    val goalWeight: Double = 70.0,
    val dailyCalories: Int = 2200,
    val proteinGoal: Double = 150.0,
    val carbGoal: Double = 300.0,
    val fatGoal: Double = 70.0,
    val waterGoal: Int = 2500, // ml
    
    // Preferences
    val dietType: String = "Hepsi",
    val unitSystem: String = "Metric", // Metric, Imperial
    val remindersEnabled: Boolean = true
)
