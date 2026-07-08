package com.org.gestor_tareas.domain.repository

import com.org.gestor_tareas.domain.model.Tarea

interface TareaRepository {
    suspend fun getTareaById(id: String): Tarea
    suspend fun iniciarTrabajo(id: String): Tarea // devuelve la tarea con estado actualizado
}
