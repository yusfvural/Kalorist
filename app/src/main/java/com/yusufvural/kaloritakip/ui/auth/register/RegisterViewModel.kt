package com.yusufvural.kaloritakip.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufvural.kaloritakip.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val userRepository: com.yusufvural.kaloritakip.domain.UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NameChanged -> {
                _state.update { it.copy(name = event.name, error = null) }
            }
            is RegisterEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, error = null) }
            }
            is RegisterEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, error = null) }
            }
            is RegisterEvent.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = event.confirmPassword, error = null) }
            }
            is RegisterEvent.RegisterClicked -> {
                register()
            }
            is RegisterEvent.ErrorDismissed -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun register() {
        val currentState = _state.value
        if (currentState.password != currentState.confirmPassword) {
            _state.update { it.copy(error = "Şifreler eşleşmiyor") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = registerUseCase(currentState.name, currentState.email, currentState.password)
            
            result.onSuccess {
                // Save user locally
                userRepository.saveUser(com.yusufvural.kaloritakip.model.UserEntity(name = currentState.name))
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }
}

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed class RegisterEvent {
    data class NameChanged(val name: String) : RegisterEvent()
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent()
    object RegisterClicked : RegisterEvent()
    object ErrorDismissed : RegisterEvent()
}
