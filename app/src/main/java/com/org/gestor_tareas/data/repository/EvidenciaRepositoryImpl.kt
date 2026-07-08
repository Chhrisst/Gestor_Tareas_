package com.org.gestor_tareas.data.repository

import com.org.gestor_tareas.data.remote.datasource.EvidenciaRemoteDataSource
import com.org.gestor_tareas.data.remote.dto.RegistrarEvidenciaRequestDto
import com.org.gestor_tareas.domain.repository.EvidenciaRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EvidenciaRepositoryImpl(
    private val remoteDataSource: EvidenciaRemoteDataSource,
    private val httpClient: OkHttpClient = OkHttpClient()
) : EvidenciaRepository {

    override suspend fun subirEvidencia(tareaId: String, archivo: File): String {
        val fileName = "evidencia_${tareaId}_${System.currentTimeMillis()}.jpg"

        val presigned = remoteDataSource.obtenerUrlPresignada(tareaId, fileName)
            ?: throw Exception("No se pudo obtener la URL de subida")

        val body = archivo.asRequestBody("image/jpeg".toMediaType())
        val request = Request.Builder()
            .url(presigned.uploadUrl)
            .put(body)
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("Error al subir la evidencia a AWS (código ${response.code})")
        }

        // 3. Avisamos al backend que ya se subió, para que la registre en la BD
        remoteDataSource.registrarEvidencia(RegistrarEvidenciaRequestDto(tareaId, presigned.publicUrl))

        return presigned.publicUrl
    }
}
