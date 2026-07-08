package com.org.gestor_tareas.domain.model

data class Tarea(
    val id: String,
    val cliente: String,
    val direccion: String,
    val estado: String,          // "Pendiente" | "En Progreso" | "Completada"
    val distanciaKm: Double,
    val latitudDestino: Double,   // para el mapa
    val longitudDestino: Double,
    val monitoreoAwsActivo: Boolean = true
)
