package com.org.gestor_tareas.data.repository

import com.org.gestor_tareas.data.mapper.toDomain
import com.org.gestor_tareas.data.remote.datasource.TrabajoRemoteDataSource
import com.org.gestor_tareas.data.remote.dto.CreateTrabajoRequest
import com.org.gestor_tareas.domain.model.Trabajo
import com.org.gestor_tareas.domain.repository.TrabajoRepository

class TrabajoRepositoryImpl(private val remoteDataSource: TrabajoRemoteDataSource) : TrabajoRepository {

    override suspend fun getTrabajos(): List<Trabajo> {
        return remoteDataSource.getTrabajos()
            ?.data
            ?.items
            ?.map { it.toDomain() }
            ?: emptyList()
    }

    override suspend fun addTrabajo(nombreCliente: String, servicio: String, direccion: String, fecha: String, horario: String, tecnicoId: Int, tecnicoNombre: String) {
        remoteDataSource.addTrabajo(CreateTrabajoRequest(nombreCliente, servicio, direccion, fecha, horario, tecnicoId, tecnicoNombre))
    }
}
