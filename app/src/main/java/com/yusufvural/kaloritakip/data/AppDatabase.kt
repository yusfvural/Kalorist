package com.yusufvural.kaloritakip.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.ExerciseEntry
import com.yusufvural.kaloritakip.model.WaterEntry

@Database(entities = [FoodEntry::class, ExerciseEntry::class, WaterEntry::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun waterDao(): WaterDao

    companion object {
        const val DATABASE_NAME = "kaloritakip_db"
    }
}
