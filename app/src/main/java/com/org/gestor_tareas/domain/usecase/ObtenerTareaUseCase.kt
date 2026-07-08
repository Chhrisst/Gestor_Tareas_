package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.domain.repository.TareaRepository

class ObtenerTareaUseCase(private val repository: TareaRepository) {
    suspend operator fun invoke(id: String) = repository.getTareaById(id)
}
