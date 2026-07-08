package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.domain.repository.TareaRepository

class IniciarTrabajoUseCase(private val repository: TareaRepository) {
    suspend operator fun invoke(id: String) = repository.iniciarTrabajo(id)
}
