package com.org.gestor_tareas.domain.model

data class Trabajo(
    val id: String,
    val nombreCliente: String,
    val servicio: String,
    val direccion: String,
    val fecha: String,
    val horario: String,
    val tecnicoId: Int,
    val tecnicoNombre: String
)
