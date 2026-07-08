package com.org.gestor_tareas.ui.pantallas.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.usecase.GoogleLoginUseCase
import com.org.gestor_tareas.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, onSuccess: (String, String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(email, password)
            if (result.isSuccess) {
                val authResponse = result.getOrNull()
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                onSuccess(authResponse?.rol ?: "PENDIENTE", authResponse?.nombre ?: "Usuario")
            } else {
                _uiState.update { it.copy(isLoading = false, error = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun loginGoogle(idToken: String, onSuccess: (String, String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = googleLoginUseCase(idToken)
            if (result.isSuccess) {
                val authResponse = result.getOrNull()
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                onSuccess(authResponse?.rol ?: "PENDIENTE", authResponse?.nombre ?: "Usuario")
            } else {
                _uiState.update { it.copy(isLoading = false, error = result.exceptionOrNull()?.message) }
            }
        }
    }
}
