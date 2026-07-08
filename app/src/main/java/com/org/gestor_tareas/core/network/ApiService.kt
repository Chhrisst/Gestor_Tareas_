package com.org.gestor_tareas.core.network

import com.org.gestor_tareas.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("tecnicos")
    suspend fun getTecnicos(): Response<ApiResponse<TecnicosDataDto>>

    @POST("tecnicos")
    suspend fun addTecnico(@Body request: CreateTecnicoRequest): Response<TecnicoDto>

    @PUT("tecnicos/{id}")
    suspend fun updateTecnico(@Path("id") id: Int, @Body request: UpdateTecnicoRequest): Response<TecnicoDto>

    @DELETE("tecnicos/{id}")
    suspend fun deleteTecnico(@Path("id") id: Int): Response<Unit>

    @GET("ordenes")
    suspend fun getOrdenes(): Response<ApiResponse<OrdenDataDto>>

    @POST("ordenes")
    suspend fun addOrden(@Body request: CreateOrdenRequest): Response<OrdenDto>

    @PUT("ordenes/{id}")
    suspend fun updateOrden(@Path("id") id: Int, @Body request: UpdateOrdenRequest): Response<OrdenDto>

    @DELETE("ordenes/{id}")
    suspend fun deleteOrden(@Path("id") id: Int): Response<Unit>

    @GET("evidencias/presigned-url")
    suspend fun obtenerUrlPresignada(
        @Query("tareaId") tareaId: String,
        @Query("fileName") fileName: String
    ): Response<PresignedUrlResponseDto>

    @POST("evidencias")
    suspend fun registrarEvidencia(@Body request: RegistrarEvidenciaRequestDto): Response<Unit>

    @POST("auth")
    suspend fun autenticar(@Body request: AuthRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth")
    suspend fun verificarSesion(@Body request: VerificarSesionRequest): Response<ApiResponse<SessionStatusResponse>>

    @GET("trabajos")
    suspend fun getTrabajos(): Response<ApiResponse<TrabajosDataDto>>

    @POST("trabajos")
    suspend fun addTrabajo(@Body request: CreateTrabajoRequest): Response<TrabajoDto>

    @POST("auth")
    suspend fun listarUsuarios(@Body request: ListarUsuariosRequest): Response<ApiResponse<UsuariosDataDto>>

    @POST("auth")
    suspend fun eliminarUsuario(@Body request: EliminarUsuarioRequest): Response<ApiResponse<Unit>>

    @POST("auth")
    suspend fun cambiarRol(@Body request: CambiarRolRequest): Response<ApiResponse<Any>>
}
