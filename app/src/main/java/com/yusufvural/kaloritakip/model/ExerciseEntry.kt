package com.yusufvural.kaloritakip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.Index

@Entity(
    tableName = "exercise_table",
    indices = [Index(value = ["userId", "timestamp"])]
)
data class ExerciseEntry(
    @PrimaryKey val id: String,
    val name: String,
    val caloriesBurnt: Int,
    val durationMinutes: Int,
    val userId: String,
    val timestamp: Long = System.currentTimeMillis()
)
