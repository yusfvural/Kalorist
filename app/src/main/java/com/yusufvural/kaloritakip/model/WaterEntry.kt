package com.yusufvural.kaloritakip.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

import androidx.room.Index

@Entity(
    tableName = "water_table",
    indices = [Index(value = ["userId", "timestamp"])]
)
data class WaterEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val amountMl: Int,
    val userId: String,
    val timestamp: Long = System.currentTimeMillis()
)
