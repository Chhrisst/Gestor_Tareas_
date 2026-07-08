package com.org.gestor_tareas.data.remote.dto

data class PresignedUrlResponseDto(
    val uploadUrl: String,
    val publicUrl: String 
)

data class RegistrarEvidenciaRequestDto(
    val tareaId: String,
    val urlEvidencia: String
)
