package com.org.gestor_tareas.data.remote.dto

data class GoogleLoginRequest(
    val idToken: String,
    val accion: String = "google-login"
)
