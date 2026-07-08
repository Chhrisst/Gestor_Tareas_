package com.org.gestor_tareas.domain.usecase

import com.org.gestor_tareas.domain.repository.UsuarioRepository

data class UsuarioUseCases(
    val listarUsuarios: ListarUsuariosUseCase,
    val eliminarUsuario: EliminarUsuarioUseCase,
    val registrarUsuario: RegistrarUsuarioAdminUseCase
)

class ListarUsuariosUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke() = repository.listarUsuarios()
}

class EliminarUsuarioUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke(email: String) = repository.eliminarUsuario(email)
}

class RegistrarUsuarioAdminUseCase(private val repository: UsuarioRepository) {
    suspend operator fun invoke(nombre: String, email: String, password: String, rol: String) =
        repository.registrarUsuario(nombre, email, password, rol)
}
