package com.org.gestor_tareas.ui.pantallas.auth

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
