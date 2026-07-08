package com.org.gestor_tareas.data.repository

import com.org.gestor_tareas.data.remote.datasource.UsuarioRemoteDataSource
import com.org.gestor_tareas.domain.model.Tecnico
import com.org.gestor_tareas.domain.repository.TecnicoRepository

class TecnicoRepositoryImpl(
    private val usuarioDataSource: UsuarioRemoteDataSource
) : TecnicoRepository {

    override suspend fun getTecnicos(): List<Tecnico> {
        return try {
            usuarioDataSource.listarUsuarios()
                ?.data
                ?.items
                ?.filter { it.rol.uppercase() == "TECNICO" }
                ?.map {
                    Tecnico(
                        id = it.email.hashCode(),
                        nombre = it.nombre,
                        rol = "Técnico"
                    )
                } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addTecnico(nombre: String, rol: String) {
        // Obsoleto: Gestión realizada mediante UsuarioRepository
    }

    override suspend fun updateTecnico(id: Int, nombre: String, rol: String) {
        // Obsoleto: Gestión realizada mediante UsuarioRepository
    }

    override suspend fun deleteTecnico(id: Int) {
        // Obsoleto: Gestión realizada mediante UsuarioRepository
    }
}