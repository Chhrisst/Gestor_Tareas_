package com.org.gestor_tareas.data.remote.dto

data class ApiResponse<T>(val data: T)
data class TecnicosDataDto(val items: List<TecnicoDto>)

data class TecnicoDto(val id: Int, val nombre: String, val rol: String)
data class CreateTecnicoRequest(val nombre: String, val rol: String)
data class UpdateTecnicoRequest(val nombre: String, val rol: String)