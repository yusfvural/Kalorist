package com.yusufvural.kaloritakip.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.ExerciseEntry
import com.yusufvural.kaloritakip.model.WaterEntry
import com.yusufvural.kaloritakip.model.UserEntity

@Database(entities = [FoodEntry::class, ExerciseEntry::class, WaterEntry::class, UserEntity::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun waterDao(): WaterDao
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "kaloritakip_db"
        
        val MIGRATION_6_7 = object : androidx.room.migration.Migration(6, 7) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Since we are moving from a single-user local DB to a multi-user structure with UID keys,
                // and we don't have a way to map old anonymous data to a specific Firebase UID,
                // we will recreate the tables. This is effectively destructive but explicit.
                
                // Drop old tables
                database.execSQL("DROP TABLE IF EXISTS `food_entries`")
                database.execSQL("DROP TABLE IF EXISTS `exercise_table`")
                database.execSQL("DROP TABLE IF EXISTS `water_table`")
                database.execSQL("DROP TABLE IF EXISTS `user_table`")
                
                // Create new tables with userId columns and proper PKs
                
                // FoodEntry
                database.execSQL("CREATE TABLE IF NOT EXISTS `food_entries` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `calories` INTEGER NOT NULL, `protein` REAL NOT NULL, `carbs` REAL NOT NULL, `fat` REAL NOT NULL, `mealType` TEXT NOT NULL, `userId` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))")
                
                // ExerciseEntry
                database.execSQL("CREATE TABLE IF NOT EXISTS `exercise_table` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `caloriesBurnt` INTEGER NOT NULL, `durationMinutes` INTEGER NOT NULL, `userId` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))")
                
                // WaterEntry
                database.execSQL("CREATE TABLE IF NOT EXISTS `water_table` (`id` TEXT NOT NULL, `amountMl` INTEGER NOT NULL, `userId` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))")
                
                // UserEntity
                database.execSQL("CREATE TABLE IF NOT EXISTS `user_table` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `age` INTEGER NOT NULL, `height` INTEGER NOT NULL, `currentWeight` REAL NOT NULL, `gender` TEXT NOT NULL, `activityLevel` REAL NOT NULL, `goalWeight` REAL NOT NULL, `dailyCalories` INTEGER NOT NULL, `proteinGoal` REAL NOT NULL, `carbGoal` REAL NOT NULL, `fatGoal` REAL NOT NULL, `dietType` TEXT NOT NULL, `unitSystem` TEXT NOT NULL, `remindersEnabled` INTEGER NOT NULL, `waterGoal` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        val MIGRATION_7_8 = object : androidx.room.migration.Migration(7, 8) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Add indices for performance
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_food_entries_userId_timestamp` ON `food_entries` (`userId`, `timestamp`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_table_userId_timestamp` ON `exercise_table` (`userId`, `timestamp`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_water_table_userId_timestamp` ON `water_table` (`userId`, `timestamp`)")
            }
        }
    }
}
