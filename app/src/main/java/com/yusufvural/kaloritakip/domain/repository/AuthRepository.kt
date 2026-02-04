package com.yusufvural.kaloritakip.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<FirebaseUser?>
    
    suspend fun login(email: String, pass: String): Result<FirebaseUser>
    suspend fun register(name: String, email: String, pass: String): Result<FirebaseUser>
    fun logout()
}
