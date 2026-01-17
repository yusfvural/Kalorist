package com.yusufvural.kaloritakip.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yusufvural.kaloritakip.model.FoodEntry

@Database(entities = [FoodEntry::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        const val DATABASE_NAME = "kaloritakip_db"
    }
}
