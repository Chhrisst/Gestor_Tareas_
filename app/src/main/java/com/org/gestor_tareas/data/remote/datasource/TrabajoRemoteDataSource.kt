package com.org.gestor_tareas.data.remote.datasource

import com.org.gestor_tareas.core.network.ApiService
import com.org.gestor_tareas.data.remote.dto.*

class TrabajoRemoteDataSource(private val api: ApiService) {
    suspend fun getTrabajos(): ApiResponse<TrabajosDataDto>? = api.getTrabajos().body()
    suspend fun addTrabajo(request: CreateTrabajoRequest): TrabajoDto? = api.addTrabajo(request).body()
}
