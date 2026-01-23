package com.yusufvural.kaloritakip.data

import com.yusufvural.kaloritakip.domain.UserRepository
import com.yusufvural.kaloritakip.model.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override fun getUser(): Flow<UserEntity?> = userDao.getUser()

    override suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }
}
