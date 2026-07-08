package com.org.gestor_tareas.data.remote.dto

data class TrabajosDataDto(val items: List<TrabajoDto>)

data class TrabajoDto(
    val id: String,
    val nombreCliente: String,
    val servicio: String,
    val direccion: String,
    val fecha: String,
    val horario: String,
    val tecnicoId: Int,
    val tecnicoNombre: String
)
data class CreateTrabajoRequest(
    val nombreCliente: String,
    val servicio: String,
    val direccion: String,
    val fecha: String,
    val horario: String,
    val tecnicoId: Int,
    val tecnicoNombre: String
)
