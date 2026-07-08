package com.org.gestor_tareas.data.repository

import com.org.gestor_tareas.data.remote.datasource.TrabajoRemoteDataSource
import com.org.gestor_tareas.domain.model.EstadoOrdenes
import com.org.gestor_tareas.domain.repository.OrdenesRepository

/**
 * Implementación real que cuenta los trabajos de DynamoDB (tabla Trabajos).
 * Como la tabla Trabajos no tiene campo 'estado', todos los registros
 * se cuentan como el total de trabajos programados.
 */
class OrdenesRepositoryImpl(
    private val trabajoRemoteDataSource: TrabajoRemoteDataSource
) : OrdenesRepository {

    override suspend fun getEstadoOrdenes(): EstadoOrdenes {
        return try {
            val response = trabajoRemoteDataSource.getTrabajos()
            val items = response?.data?.items ?: emptyList()
            val total = items.size
            // Sin campo estado en el backend: todos son Programados
            EstadoOrdenes(
                totalTareas = total,
                finalizadas = 0,
                enProceso = 0,
                pendientes = total
            )
        } catch (e: Exception) {
            EstadoOrdenes(totalTareas = 0, finalizadas = 0, enProceso = 0, pendientes = 0)
        }
    }
}
