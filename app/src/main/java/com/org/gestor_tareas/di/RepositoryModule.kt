package com.org.gestor_tareas.di

import android.content.Context
import com.org.gestor_tareas.data.local.TokenDataStore
import com.org.gestor_tareas.data.remote.datasource.EvidenciaRemoteDataSource
import com.org.gestor_tareas.data.remote.datasource.OrdenRemoteDataSource
import com.org.gestor_tareas.data.remote.datasource.TecnicoRemoteDataSource
import com.org.gestor_tareas.data.repository.AuthRepositoryImpl
import com.org.gestor_tareas.data.repository.EvidenciaRepositoryImpl
import com.org.gestor_tareas.data.repository.OrdenRepositoryImpl
import com.org.gestor_tareas.data.repository.OrdenesRepositoryImpl
import com.org.gestor_tareas.data.repository.TareaRepositoryImpl
import com.org.gestor_tareas.data.repository.TecnicoRepositoryImpl
import com.org.gestor_tareas.domain.repository.AuthRepository
import com.org.gestor_tareas.domain.repository.EvidenciaRepository
import com.org.gestor_tareas.domain.repository.OrdenRepository
import com.org.gestor_tareas.domain.repository.OrdenesRepository
import com.org.gestor_tareas.domain.repository.TareaRepository
import com.org.gestor_tareas.domain.repository.TecnicoRepository

class RepositoryModule(private val context: Context, networkModule: NetworkModule) {
    private val remoteDataSource by lazy {
        TecnicoRemoteDataSource(networkModule.apiService)
    }

    private val ordenRemoteDataSource by lazy {
        OrdenRemoteDataSource(networkModule.apiService)
    }

    private val evidenciaRemoteDataSource by lazy {
        EvidenciaRemoteDataSource(networkModule.apiService)
    }

    private val trabajoRemoteDataSource by lazy {
        com.org.gestor_tareas.data.remote.datasource.TrabajoRemoteDataSource(networkModule.apiService)
    }

    private val usuarioRemoteDataSource by lazy {
        com.org.gestor_tareas.data.remote.datasource.UsuarioRemoteDataSource(networkModule.apiService)
    }

    // tokenDataStore y authRepository deben declararse antes de usuarioRepository
    val tokenDataStore by lazy {
        TokenDataStore(context)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(networkModule.apiService, tokenDataStore)
    }

    val tecnicoRepository: TecnicoRepository by lazy {
        TecnicoRepositoryImpl(usuarioRemoteDataSource)
    }

    val trabajoRepository: com.org.gestor_tareas.domain.repository.TrabajoRepository by lazy {
        com.org.gestor_tareas.data.repository.TrabajoRepositoryImpl(trabajoRemoteDataSource)
    }

    val ordenRepository: OrdenRepository by lazy {
        OrdenRepositoryImpl(ordenRemoteDataSource)
    }

    val ordenesRepository: OrdenesRepository by lazy {
        OrdenesRepositoryImpl(trabajoRemoteDataSource)
    }

    val usuarioRepository: com.org.gestor_tareas.domain.repository.UsuarioRepository by lazy {
        com.org.gestor_tareas.data.repository.UsuarioRepositoryImpl(usuarioRemoteDataSource, authRepository)
    }

    val tareaRepository: TareaRepository by lazy {
        TareaRepositoryImpl()
    }

    val evidenciaRepository: EvidenciaRepository by lazy {
        EvidenciaRepositoryImpl(evidenciaRemoteDataSource)
    }
}
