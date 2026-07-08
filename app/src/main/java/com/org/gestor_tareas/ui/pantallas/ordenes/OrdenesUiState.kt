package com.org.gestor_tareas.ui.pantallas.ordenes

import com.org.gestor_tareas.domain.model.EstadoOrdenes

data class OrdenesUiState(
    val estado: EstadoOrdenes? = null,
    val isLoading: Boolean = false
)
