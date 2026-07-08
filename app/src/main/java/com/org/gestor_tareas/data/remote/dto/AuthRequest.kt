package com.org.gestor_tareas.data.remote.dto

data class AuthRequest(
    val accion: String,
    val email: String,
    val password: String,
    val nombre: String? = null
)
