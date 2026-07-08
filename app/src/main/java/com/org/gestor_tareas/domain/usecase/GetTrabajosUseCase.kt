package com.org.gestor_tareas.domain.usecase
import com.org.gestor_tareas.domain.repository.TrabajoRepository

class GetTrabajosUseCase(private val repository: TrabajoRepository) {
    suspend operator fun invoke() = repository.getTrabajos()
}
