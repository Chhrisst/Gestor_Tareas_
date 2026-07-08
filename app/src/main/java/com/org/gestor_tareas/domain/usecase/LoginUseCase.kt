package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.data.remote.dto.AuthRequest
import com.org.gestor_tareas.data.remote.dto.AuthResponse
import com.org.gestor_tareas.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = repository.autenticar(AuthRequest("login", email, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!.data
                repository.guardarDatosAutenticacion(authResponse.token, authResponse.rol, authResponse.nombre, authResponse.email)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Error en el inicio de sesión: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
