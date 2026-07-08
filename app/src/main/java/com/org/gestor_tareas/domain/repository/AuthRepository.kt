package com.org.gestor_tareas.domain.repository

import com.org.gestor_tareas.data.remote.dto.*
import retrofit2.Response

interface AuthRepository {
    suspend fun autenticar(request: AuthRequest): Response<ApiResponse<AuthResponse>>
    suspend fun googleLogin(request: GoogleLoginRequest): Response<ApiResponse<AuthResponse>>
    suspend fun verificarSesion(email: String): Response<ApiResponse<SessionStatusResponse>>
    suspend fun guardarDatosAutenticacion(token: String, rol: String, nombre: String, email: String)
    suspend fun cambiarRol(email: String, nuevoRol: String): Response<ApiResponse<Any>>
}
