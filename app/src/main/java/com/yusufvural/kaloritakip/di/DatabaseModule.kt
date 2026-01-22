package com.yusufvural.kaloritakip.di

import android.content.Context
import androidx.room.Room
import com.yusufvural.kaloritakip.data.AppDatabase
import com.yusufvural.kaloritakip.data.FoodDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFoodDao(database: AppDatabase): FoodDao {
        return database.foodDao()
    }

    @Provides
    fun provideExerciseDao(database: AppDatabase): com.yusufvural.kaloritakip.data.ExerciseDao {
        return database.exerciseDao()
    }

    @Provides
    fun provideWaterDao(database: AppDatabase): com.yusufvural.kaloritakip.data.WaterDao {
        return database.waterDao()
    }
}
