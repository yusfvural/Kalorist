package com.yusufvural.kaloritakip.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yusufvural.kaloritakip.model.FoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_entries WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllEntries(userId: String): Flow<List<FoodEntry>>

    @Query("SELECT * FROM food_entries WHERE userId = :userId AND timestamp >= :startOfDay AND timestamp < :endOfDay ORDER BY timestamp DESC")
    fun getEntriesForDay(userId: String, startOfDay: Long, endOfDay: Long): Flow<List<FoodEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: FoodEntry)

    @Delete
    suspend fun deleteEntry(entry: FoodEntry)

    @Query("DELETE FROM food_entries WHERE userId = :userId")
    suspend fun clearAll(userId: String)
}
