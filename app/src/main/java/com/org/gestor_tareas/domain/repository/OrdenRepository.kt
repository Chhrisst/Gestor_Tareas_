package com.org.gestor_tareas.domain.repository

import com.org.gestor_tareas.domain.model.OrdenTrabajo

interface OrdenRepository {

    suspend fun getOrdenes(): List<OrdenTrabajo>

    suspend fun addOrden(servicio: String, clienteNombre: String, direccion: String, horaAtencion: String, fecha: String, estado: String)

    suspend fun updateOrden(id: Int, servicio: String, clienteNombre: String, direccion: String, horaAtencion: String, fecha: String, estado: String)

    suspend fun deleteOrden(id: Int)
}
