package com.yusufvural.kaloritakip.di

import com.yusufvural.kaloritakip.data.FoodRepositoryImpl
import com.yusufvural.kaloritakip.domain.FoodRepository
import com.yusufvural.kaloritakip.domain.UserRepository
import com.yusufvural.kaloritakip.data.UserRepositoryImpl
import com.yusufvural.kaloritakip.data.repository.AuthRepositoryImpl
import com.yusufvural.kaloritakip.domain.repository.AuthRepository
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

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
