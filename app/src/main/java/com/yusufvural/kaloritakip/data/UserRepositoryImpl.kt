package com.yusufvural.kaloritakip.data

import com.yusufvural.kaloritakip.domain.UserRepository
import com.yusufvural.kaloritakip.model.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val auth: com.google.firebase.auth.FirebaseAuth
) : UserRepository {
    
    private val currentUserId: String?
        get() = auth.currentUser?.uid
        
    override fun getUser(): Flow<UserEntity?> {
        val uid = currentUserId ?: return kotlinx.coroutines.flow.emptyFlow()
        return userDao.getUser(uid)
    }

    override suspend fun saveUser(user: UserEntity) {
        val uid = currentUserId ?: return
        // Ensure the ID matches the authenticated user
        userDao.insertUser(user.copy(id = uid))
    }
}
