package com.org.gestor_tareas.ui.pantallas.tareas

import com.org.gestor_tareas.domain.model.Tarea

data class TareaUiState(
    val tarea: Tarea? = null,
    val isLoading: Boolean = false
)
