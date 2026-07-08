package com.org.gestor_tareas.data.remote.dto

data class VerificarSesionRequest(
    val email: String,
    val accion: String = "verificar-sesion"
)

data class SessionStatusResponse(
    val email: String,
    val rol: String,
    val nombre: String,
    val activo: Boolean
)
