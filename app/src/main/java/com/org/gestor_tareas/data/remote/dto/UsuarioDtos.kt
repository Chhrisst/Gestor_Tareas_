package com.org.gestor_tareas.data.remote.dto

data class UsuariosDataDto(val items: List<UsuarioDto>)

data class UsuarioDto(
    val email: String,
    val nombre: String,
    val rol: String,
    val metodo: String? = "email"
)

data class EliminarUsuarioRequest(
    val accion: String = "eliminar-usuario",
    val email: String
)

data class CambiarRolRequest(
    val accion: String = "cambiar-rol",
    val email: String,
    val nuevoRol: String
)

data class ListarUsuariosRequest(
    val accion: String = "listar-usuarios"
)

