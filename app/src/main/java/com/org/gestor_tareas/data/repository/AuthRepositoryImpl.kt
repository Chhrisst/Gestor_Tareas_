package com.org.gestor_tareas.data.repository

import com.org.gestor_tareas.core.network.ApiService
import com.org.gestor_tareas.data.local.TokenDataStore
import com.org.gestor_tareas.data.remote.dto.*
import com.org.gestor_tareas.domain.repository.AuthRepository
import retrofit2.Response

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {
    override suspend fun autenticar(request: AuthRequest): Response<ApiResponse<AuthResponse>> {
        return apiService.autenticar(request)
    }

    override suspend fun googleLogin(request: GoogleLoginRequest): Response<ApiResponse<AuthResponse>> {
        return apiService.googleLogin(request)
    }

    override suspend fun verificarSesion(email: String): Response<ApiResponse<SessionStatusResponse>> {
        return apiService.verificarSesion(VerificarSesionRequest(email))
    }

    override suspend fun guardarDatosAutenticacion(token: String, rol: String, nombre: String, email: String) {
        tokenDataStore.saveAuthData(token, rol, nombre, email)
    }

    override suspend fun cambiarRol(email: String, nuevoRol: String): Response<ApiResponse<Any>> {
        return apiService.cambiarRol(
            com.org.gestor_tareas.data.remote.dto.CambiarRolRequest(email = email, nuevoRol = nuevoRol)
        )
    }
}
