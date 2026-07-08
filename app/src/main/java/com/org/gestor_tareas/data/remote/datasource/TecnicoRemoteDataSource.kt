package com.org.gestor_tareas.data.remote.datasource

import com.org.gestor_tareas.core.network.ApiService
import com.org.gestor_tareas.data.remote.dto.*

class TecnicoRemoteDataSource(private val api: ApiService) {
    suspend fun getTecnicos(): ApiResponse<TecnicosDataDto>? = api.getTecnicos().body()
    suspend fun addTecnico(request: CreateTecnicoRequest): TecnicoDto? = api.addTecnico(request).body()
    suspend fun updateTecnico(id: Int, request: UpdateTecnicoRequest): TecnicoDto? = api.updateTecnico(id, request).body()
    suspend fun deleteTecnico(id: Int) { api.deleteTecnico(id) }
}