package com.yusufvural.kaloritakip.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yusufvural.kaloritakip.model.WaterEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_table WHERE timestamp >= :dayStart AND timestamp < :dayEnd ORDER BY timestamp ASC")
    fun getWaterForDay(dayStart: Long, dayEnd: Long): Flow<List<WaterEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWater(entry: WaterEntry)

    @Delete
    suspend fun deleteWater(entry: WaterEntry)

    @Query("SELECT SUM(amountMl) FROM water_table WHERE timestamp >= :dayStart AND timestamp < :dayEnd")
    fun getTotalWaterForDay(dayStart: Long, dayEnd: Long): Flow<Int?>
}
