package com.org.gestor_tareas.domain.model

data class OrdenTrabajo (
    val id:Int,
    val codigo: String,
    val servicio: String,
    val clienteNombre: String,
    val direccion: String,
    val horaAtencion: String,
    val fecha: String,
    val estado: String
)
