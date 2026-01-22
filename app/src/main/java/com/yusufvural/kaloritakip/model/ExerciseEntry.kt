package com.yusufvural.kaloritakip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
data class ExerciseEntry(
    @PrimaryKey val id: String,
    val name: String,
    val caloriesBurnt: Int,
    val durationMinutes: Int,
    val timestamp: Long = System.currentTimeMillis()
)
