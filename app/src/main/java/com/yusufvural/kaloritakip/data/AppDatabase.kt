package com.yusufvural.kaloritakip.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.ExerciseEntry
import com.yusufvural.kaloritakip.model.WaterEntry
import com.yusufvural.kaloritakip.model.UserEntity

@Database(entities = [FoodEntry::class, ExerciseEntry::class, WaterEntry::class, UserEntity::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun waterDao(): WaterDao
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "kaloritakip_db"
    }
}
