package com.org.gestor_tareas.domain.usecase
import com.org.gestor_tareas.domain.repository.OrdenRepository

class AddOrdenUseCase(private val repository: OrdenRepository) {
    suspend operator fun invoke(servicio: String, cliente: String, direccion: String, hora: String, fecha: String, estado: String) =
        repository.addOrden(servicio, cliente, direccion, hora, fecha, estado)
}
