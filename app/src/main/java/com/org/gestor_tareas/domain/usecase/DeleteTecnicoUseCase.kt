package com.org.gestor_tareas.domain.usecase
import com.org.gestor_tareas.domain.repository.TecnicoRepository

class DeleteTecnicoUseCase(private val repository: TecnicoRepository) {
    suspend operator fun invoke(id: Int) {
        repository.deleteTecnico(id)
    }
}