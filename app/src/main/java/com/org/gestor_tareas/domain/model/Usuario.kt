package com.org.gestor_tareas.domain.model

data class Usuario(
    val email: String,
    val nombre: String,
    val rol: String,
    val metodo: String = "email"
)
