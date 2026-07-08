package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.domain.repository.EvidenciaRepository
import java.io.File

class SubirEvidenciaUseCase(private val repository: EvidenciaRepository) {
    suspend operator fun invoke(tareaId: String, archivo: File): String {
        if (tareaId.isBlank()) throw Exception("ID de tarea inválido")
        if (!archivo.exists()) throw Exception("No se encontró la foto tomada")
        return repository.subirEvidencia(tareaId, archivo)
    }
}
