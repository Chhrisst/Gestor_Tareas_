package com.org.gestor_tareas.data.remote.datasource

import com.org.gestor_tareas.core.network.ApiService
import com.org.gestor_tareas.data.remote.dto.*

class UsuarioRemoteDataSource(private val api: ApiService) {
    suspend fun listarUsuarios() = api.listarUsuarios(ListarUsuariosRequest()).body()
    suspend fun eliminarUsuario(email: String) = api.eliminarUsuario(EliminarUsuarioRequest(email = email))
}
