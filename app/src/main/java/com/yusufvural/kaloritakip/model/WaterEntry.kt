package com.yusufvural.kaloritakip.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "water_table")
data class WaterEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)
