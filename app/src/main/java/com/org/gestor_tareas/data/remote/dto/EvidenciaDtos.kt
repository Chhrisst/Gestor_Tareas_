package com.org.gestor_tareas.data.remote.dto

data class PresignedUrlResponseDto(
    val uploadUrl: String,   // URL firmada de S3 donde se hace el PUT
    val publicUrl: String    // URL pública final para guardar/mostrar la evidencia
)

data class RegistrarEvidenciaRequestDto(
    val tareaId: String,
    val urlEvidencia: String
)