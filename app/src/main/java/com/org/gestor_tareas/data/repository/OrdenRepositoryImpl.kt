package com.org.gestor_tareas.data.repository

import com.org.gestor_tareas.data.mapper.toDomain
import com.org.gestor_tareas.data.remote.datasource.OrdenRemoteDataSource
import com.org.gestor_tareas.data.remote.dto.CreateOrdenRequest
import com.org.gestor_tareas.data.remote.dto.UpdateOrdenRequest
import com.org.gestor_tareas.domain.model.OrdenTrabajo
import com.org.gestor_tareas.domain.repository.OrdenRepository

class OrdenRepositoryImpl(private val remoteDataSource: OrdenRemoteDataSource) : OrdenRepository {

    override suspend fun getOrdenes(): List<OrdenTrabajo> {
        return remoteDataSource.getOrdenes()
            ?.data
            ?.items
            ?.map { it.toDomain() } 
            ?: emptyList()
    }

    override suspend fun addOrden(servicio: String, clienteNombre: String, direccion: String, horaAtencion: String, fecha: String, estado: String) {
        val request = CreateOrdenRequest(servicio, clienteNombre, direccion, horaAtencion, fecha, estado)
        remoteDataSource.addOrden(request)
    }

    override suspend fun updateOrden(id: Int, servicio: String, clienteNombre: String, direccion: String, horaAtencion: String, fecha: String, estado: String) {
        val request = UpdateOrdenRequest(servicio, clienteNombre, direccion, horaAtencion, fecha, estado)
        remoteDataSource.updateOrden(id, request)
    }

    override suspend fun deleteOrden(id: Int) {
        remoteDataSource.deleteOrden(id)
    }
}
