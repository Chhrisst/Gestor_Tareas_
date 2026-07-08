package com.org.gestor_tareas.ui.pantallas.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun register(nombre: String, email: String, password: String, onSuccess: (String, String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = registerUseCase(nombre, email, password)
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
