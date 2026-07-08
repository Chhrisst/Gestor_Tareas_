package com.org.gestor_tareas.domain.repository

import com.org.gestor_tareas.domain.model.Trabajo

interface TrabajoRepository {
    suspend fun getTrabajos(): List<Trabajo>
    suspend fun addTrabajo(nombreCliente: String, servicio: String, direccion: String, fecha: String, horario: String, tecnicoId: Int, tecnicoNombre: String)
}
