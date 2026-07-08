package com.org.gestor_tareas.domain.usecase
import com.org.gestor_tareas.domain.repository.TecnicoRepository

class UpdateTecnicoUseCase(private val repository: TecnicoRepository) {
    suspend operator fun invoke(id: Int, nombre: String, rol: String) {
        repository.updateTecnico(id, nombre, rol)
    }
}