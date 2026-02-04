package com.yusufvural.kaloritakip.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import com.yusufvural.kaloritakip.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, pass: String): Result<FirebaseUser> {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            return Result.failure(IllegalArgumentException("Alanlar boş bırakılamaz"))
        }
        return repository.register(name, email, pass)
    }
}
