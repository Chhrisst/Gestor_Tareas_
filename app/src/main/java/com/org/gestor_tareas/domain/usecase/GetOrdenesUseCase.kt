package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.domain.repository.OrdenRepository

class GetOrdenesUseCase (private val repository: OrdenRepository){
    suspend operator fun invoke() = repository.getOrdenes()
}
