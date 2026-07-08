package com.org.gestor_tareas.data.remote.datasource

import com.org.gestor_tareas.core.network.ApiService
import com.org.gestor_tareas.data.remote.dto.*

class OrdenRemoteDataSource(private val api: ApiService) {
    suspend fun getOrdenes(): ApiResponse<OrdenDataDto>? = api.getOrdenes().body()
    suspend fun addOrden(request: CreateOrdenRequest): OrdenDto? = api.addOrden(request).body()
    suspend fun updateOrden(id: Int, request: UpdateOrdenRequest): OrdenDto? = api.updateOrden(id, request).body()
    suspend fun deleteOrden(id: Int) { api.deleteOrden(id) }
}
