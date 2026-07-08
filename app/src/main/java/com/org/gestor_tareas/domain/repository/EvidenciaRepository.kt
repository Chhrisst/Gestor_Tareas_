package com.org.gestor_tareas.domain.repository

import java.io.File

interface EvidenciaRepository {
    // Sube el archivo a AWS S3 (vía presigned URL) y devuelve la URL pública final
    suspend fun subirEvidencia(tareaId: String, archivo: File): String
}
