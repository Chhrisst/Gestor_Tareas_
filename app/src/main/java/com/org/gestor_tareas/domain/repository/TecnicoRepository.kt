package com.org.gestor_tareas.domain.repository

import com.org.gestor_tareas.domain.model.Tecnico

interface TecnicoRepository {
    suspend fun getTecnicos(): List<Tecnico>
    suspend fun addTecnico(nombre: String, rol: String)
    suspend fun updateTecnico(id: Int, nombre: String, rol: String)
    suspend fun deleteTecnico(id: Int)
}