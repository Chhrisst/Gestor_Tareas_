package com.org.gestor_tareas.domain.usecase
import com.org.gestor_tareas.domain.repository.TrabajoRepository

class AddTrabajoUseCase(private val repository: TrabajoRepository) {
    suspend operator fun invoke(nombreCliente: String, servicio: String, direccion: String, fecha: String, horario: String, tecnicoId: Int, tecnicoNombre: String) {
        repository.addTrabajo(nombreCliente, servicio, direccion, fecha, horario, tecnicoId, tecnicoNombre)
    }
}
