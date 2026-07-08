package com.org.gestor_tareas.di

import android.content.Context
import com.org.gestor_tareas.ui.pantallas.auth.LoginViewModel
import com.org.gestor_tareas.ui.pantallas.auth.RegisterViewModel

class AppContainer(private val context: Context) {
    val networkModule by lazy {
        NetworkModule()
    }

    val repositoryModule by lazy {
        RepositoryModule(context, networkModule)
    }

    val tokenDataStore by lazy {
        repositoryModule.tokenDataStore
    }

    val useCaseModule by lazy {
        UseCaseModule(repositoryModule)
    }

    val tecnicoViewModel by lazy {
        com.org.gestor_tareas.ui.pantallas.tecnicos.TecnicoViewModel(useCaseModule.tecnicoUseCases)
    }

    val loginViewModel by lazy {
        LoginViewModel(useCaseModule.loginUseCase, useCaseModule.googleLoginUseCase)
    }

    val registerViewModel by lazy {
        RegisterViewModel(useCaseModule.registerUseCase)
    }

    val ordenTecnicoViewModel by lazy {
        com.org.gestor_tareas.ui.pantallas.tecnicos.OrdenTecnicoViewModel(useCaseModule.ordenUseCases)
    }

    val asignarOrdenViewModel by lazy {
        com.org.gestor_tareas.ui.pantallas.tecnicos.AsignarOrdenViewModel(useCaseModule.ordenUseCases)
    }

    val asignarTrabajoViewModel by lazy {
        com.org.gestor_tareas.ui.pantallas.trabajos.AsignarTrabajoViewModel(useCaseModule.tecnicoUseCases, useCaseModule.trabajoUseCases)
    }

    val tareaViewModel by lazy {
        com.org.gestor_tareas.ui.pantallas.tareas.TareaViewModel(useCaseModule.tareaUseCases)
    }

    val evidenciaViewModel by lazy {
        com.org.gestor_tareas.ui.pantallas.evidencia.EvidenciaViewModel(useCaseModule.subirEvidenciaUseCase)
    }

    val ordenesViewModel by lazy {
        com.org.gestor_tareas.ui.pantallas.ordenes.OrdenesViewModel(useCaseModule.obtenerEstadoOrdenesUseCase)
    }

    val dashboardViewModel by lazy {
        com.org.gestor_tareas.ui.pantallas.DashboardViewModel(
            useCaseModule.tecnicoUseCases,
            useCaseModule.obtenerEstadoOrdenesUseCase
        )
    }

    val usuarioViewModel by lazy {
        com.org.gestor_tareas.ui.pantallas.tecnicos.UsuarioViewModel(useCaseModule.usuarioUseCases)
    }
}