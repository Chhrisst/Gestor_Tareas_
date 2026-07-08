package com.org.gestor_tareas.data.repository

import com.org.gestor_tareas.data.remote.datasource.UsuarioRemoteDataSource
import com.org.gestor_tareas.data.remote.dto.AuthRequest
import com.org.gestor_tareas.domain.model.Usuario
import com.org.gestor_tareas.domain.repository.AuthRepository
import com.org.gestor_tareas.domain.repository.UsuarioRepository

class UsuarioRepositoryImpl(
    private val usuarioDataSource: UsuarioRemoteDataSource,
    private val authRepository: AuthRepository
) : UsuarioRepository {

    override suspend fun listarUsuarios(): List<Usuario> {
        return usuarioDataSource.listarUsuarios()
            ?.data?.items
            ?.map { Usuario(email = it.email, nombre = it.nombre, rol = it.rol, metodo = it.metodo ?: "email") }
            ?: emptyList()
    }

    override suspend fun eliminarUsuario(email: String) {
        usuarioDataSource.eliminarUsuario(email)
    }

    override suspend fun registrarUsuario(nombre: String, email: String, password: String, rol: String) {
        authRepository.autenticar(
            AuthRequest(
                accion = "registro",
                email = email,
                password = password,
                nombre = nombre
            )
        )
        // Si el rol seleccionado es diferente de "Pendiente", lo actualizamos en el backend
        if (rol != "Pendiente") {
            val backendRol = when (rol) {
                "Administrador" -> "ADMIN"
                "Técnico" -> "TECNICO"
                "Deshabilitado" -> "INACTIVO"
                else -> "PENDIENTE"
            }
            authRepository.cambiarRol(email, backendRol)
        }
    }
}
