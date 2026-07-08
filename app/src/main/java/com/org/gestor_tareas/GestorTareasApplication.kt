package com.org.gestor_tareas

import android.app.Application
import com.org.gestor_tareas.di.AppContainer

class GestorTareasApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}