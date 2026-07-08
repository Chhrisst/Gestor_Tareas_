package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.domain.repository.OrdenRepository

class UpdateOrdenUseCase(private val repository: OrdenRepository) {
    suspend operator fun invoke(id: Int, servicio: String, cliente: String, direccion: String, hora: String, fecha: String, estado: String) =
        repository.updateOrden(id, servicio, cliente, direccion, hora, fecha, estado)
}
