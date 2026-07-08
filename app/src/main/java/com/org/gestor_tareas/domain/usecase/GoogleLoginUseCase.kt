package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.data.remote.dto.AuthResponse
import com.org.gestor_tareas.data.remote.dto.GoogleLoginRequest
import com.org.gestor_tareas.domain.repository.AuthRepository

class GoogleLoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(idToken: String): Result<AuthResponse> {
        return try {
            val response = repository.googleLogin(GoogleLoginRequest(idToken))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!.data
                repository.guardarDatosAutenticacion(authResponse.token, authResponse.rol, authResponse.nombre, authResponse.email)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Error en inicio con Google: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
