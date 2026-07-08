package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.data.remote.dto.AuthRequest
import com.org.gestor_tareas.data.remote.dto.AuthResponse
import com.org.gestor_tareas.domain.repository.AuthRepository
import org.json.JSONObject

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(nombre: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = repository.autenticar(AuthRequest("registro", email, password, nombre))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!.data
                repository.guardarDatosAutenticacion(authResponse.token, authResponse.rol, authResponse.nombre, authResponse.email)
                Result.success(authResponse)
            } else {
                val errorMsg = try {
                    val errorObj = JSONObject(response.errorBody()?.string() ?: "")
                    errorObj.getJSONObject("error").getString("message")
                } catch (e: Exception) {
                    "Error en el registro (${response.code()})"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
