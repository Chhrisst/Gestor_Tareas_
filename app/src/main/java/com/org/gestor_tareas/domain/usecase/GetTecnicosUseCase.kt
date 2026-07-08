package com.org.gestor_tareas.domain.usecase
import com.org.gestor_tareas.domain.repository.TecnicoRepository

class GetTecnicosUseCase(private val repository: TecnicoRepository) {
    suspend operator fun invoke() = repository.getTecnicos()
}