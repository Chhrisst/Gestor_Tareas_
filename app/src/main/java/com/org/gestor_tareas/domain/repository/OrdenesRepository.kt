package com.org.gestor_tareas.domain.repository

import com.org.gestor_tareas.domain.model.EstadoOrdenes

interface OrdenesRepository {
    suspend fun getEstadoOrdenes(): EstadoOrdenes
}
