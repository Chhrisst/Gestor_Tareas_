package com.org.gestor_tareas.data.remote.dto

/**
 * Representa la orden tal cual viene del servidor (AWS).
 */
data class OrdenDto(
    val id: Int,
    val codigo: String,
    val servicio: String,
    val cliente_nombre: String, 
    val direccion: String,
    val hora_atencion: String,
    val fecha: String,
    val estado: String
)


data class CreateOrdenRequest(
    val servicio: String,
    val cliente_nombre: String,
    val direccion: String,
    val hora_atencion: String,
    val fecha: String,
    val estado: String
)


data class UpdateOrdenRequest(
    val servicio: String,
    val cliente_nombre: String,
    val direccion: String,
    val hora_atencion: String,
    val fecha: String,
    val estado: String
)

/**
 * Clase para envolver la lista de órdenes.
 */
data class OrdenDataDto(
    val items: List<OrdenDto>
)
