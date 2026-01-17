package com.yusufvural.kaloritakip.di

import com.yusufvural.kaloritakip.data.FoodRepositoryImpl
import com.yusufvural.kaloritakip.domain.FoodRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFoodRepository(
        foodRepositoryImpl: FoodRepositoryImpl
    ): FoodRepository
}
