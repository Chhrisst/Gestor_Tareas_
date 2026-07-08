package com.org.gestor_tareas.data.remote.datasource

import com.org.gestor_tareas.core.network.ApiService
import com.org.gestor_tareas.data.remote.dto.PresignedUrlResponseDto
import com.org.gestor_tareas.data.remote.dto.RegistrarEvidenciaRequestDto

class EvidenciaRemoteDataSource (private val api: ApiService) {
    suspend fun obtenerUrlPresignada(tareaId: String, fileName: String): PresignedUrlResponseDto? =
        api.obtenerUrlPresignada(tareaId, fileName).body()

    suspend fun registrarEvidencia(request: RegistrarEvidenciaRequestDto) {
        api.registrarEvidencia(request)
    }
}