package com.org.gestor_tareas.domain.repository

import com.org.gestor_tareas.domain.model.Usuario

interface UsuarioRepository {
    suspend fun listarUsuarios(): List<Usuario>
    suspend fun eliminarUsuario(email: String)
    suspend fun registrarUsuario(nombre: String, email: String, password: String, rol: String)
}
