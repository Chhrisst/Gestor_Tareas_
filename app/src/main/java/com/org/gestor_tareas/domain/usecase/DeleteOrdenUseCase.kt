package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.domain.repository.OrdenRepository


class DeleteOrdenUseCase(private val repository: OrdenRepository) {
    suspend operator fun invoke(id: Int) = repository.deleteOrden(id)
}
