package com.org.gestor_tareas.di

import com.org.gestor_tareas.core.network.ApiService
import com.org.gestor_tareas.core.network.RetrofitClient

class NetworkModule {
    val apiService: ApiService by lazy {
        RetrofitClient.api
    }
}