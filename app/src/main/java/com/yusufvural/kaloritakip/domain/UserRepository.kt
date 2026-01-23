package com.yusufvural.kaloritakip.domain

import com.yusufvural.kaloritakip.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<UserEntity?>
    suspend fun saveUser(user: UserEntity)
}
