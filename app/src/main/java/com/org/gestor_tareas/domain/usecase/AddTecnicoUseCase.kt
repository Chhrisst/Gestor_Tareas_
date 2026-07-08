package com.org.gestor_tareas.domain.usecase
import com.org.gestor_tareas.domain.repository.TecnicoRepository

class AddTecnicoUseCase(private val repository: TecnicoRepository) {
    suspend operator fun invoke(nombre: String, rol: String) {
        repository.addTecnico(nombre, rol)
    }
}