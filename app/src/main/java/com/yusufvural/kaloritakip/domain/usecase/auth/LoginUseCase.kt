package com.yusufvural.kaloritakip.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import com.yusufvural.kaloritakip.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, pass: String): Result<FirebaseUser> {
        if (email.isBlank() || pass.isBlank()) {
            return Result.failure(IllegalArgumentException("E-posta ve şifre boş olamaz"))
        }
        return repository.login(email, pass)
    }
}
