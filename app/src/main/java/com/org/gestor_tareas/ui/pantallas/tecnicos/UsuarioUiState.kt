package com.org.gestor_tareas.ui.pantallas.tecnicos

import com.org.gestor_tareas.domain.model.Usuario

data class UsuarioUiState(
    val usuarios: List<Usuario> = emptyList(),
    val isLoading: Boolean = false,
    val mostrarDialogo: Boolean = false,
    val usuarioEditandoEmail: String? = null,
    val nombreInput: String = "",
    val emailInput: String = "",
    val passwordInput: String = "",
    val rolInput: String = "Técnico"
)
