package com.org.gestor_tareas.di

import com.org.gestor_tareas.domain.usecase.*

class UseCaseModule(repositoryModule: RepositoryModule) {
    val tecnicoUseCases by lazy {
        TecnicoUseCases(
            getTecnicos = GetTecnicosUseCase(repositoryModule.tecnicoRepository),
            addTecnico = AddTecnicoUseCase(repositoryModule.tecnicoRepository),
            updateTecnico = UpdateTecnicoUseCase(repositoryModule.tecnicoRepository),
            deleteTecnico = DeleteTecnicoUseCase(repositoryModule.tecnicoRepository)
        )
    }

    val trabajoUseCases by lazy {
        com.org.gestor_tareas.domain.usecase.TrabajoUseCases(
            getTrabajos = com.org.gestor_tareas.domain.usecase.GetTrabajosUseCase(repositoryModule.trabajoRepository),
            addTrabajo = com.org.gestor_tareas.domain.usecase.AddTrabajoUseCase(repositoryModule.trabajoRepository)
        )
    }

    val loginUseCase by lazy {
        LoginUseCase(repositoryModule.authRepository)
    }

    val googleLoginUseCase by lazy {
        GoogleLoginUseCase(repositoryModule.authRepository)
    }

    val registerUseCase by lazy {
        RegisterUseCase(repositoryModule.authRepository)
    }

    val ordenUseCases by lazy {
        OrdenUseCases(
            getOrdenes = GetOrdenesUseCase(repositoryModule.ordenRepository),
            addOrden = AddOrdenUseCase(repositoryModule.ordenRepository),
            updateOrden = UpdateOrdenUseCase(repositoryModule.ordenRepository),
            deleteOrden = DeleteOrdenUseCase(repositoryModule.ordenRepository)
        )
    }

    val tareaUseCases by lazy {
        TareaUseCases(
            obtenerTarea = ObtenerTareaUseCase(repositoryModule.tareaRepository),
            iniciarTrabajo = IniciarTrabajoUseCase(repositoryModule.tareaRepository)
        )
    }

    val subirEvidenciaUseCase by lazy {
        SubirEvidenciaUseCase(repositoryModule.evidenciaRepository)
    }

    val obtenerEstadoOrdenesUseCase by lazy {
        ObtenerEstadoOrdenesUseCase(repositoryModule.ordenesRepository)
    }

    val usuarioUseCases by lazy {
        com.org.gestor_tareas.domain.usecase.UsuarioUseCases(
            listarUsuarios = com.org.gestor_tareas.domain.usecase.ListarUsuariosUseCase(repositoryModule.usuarioRepository),
            eliminarUsuario = com.org.gestor_tareas.domain.usecase.EliminarUsuarioUseCase(repositoryModule.usuarioRepository),
            registrarUsuario = com.org.gestor_tareas.domain.usecase.RegistrarUsuarioAdminUseCase(repositoryModule.usuarioRepository)
        )
    }
}
