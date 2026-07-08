package com.org.gestor_tareas.ui.pantallas.tecnicos

import com.org.gestor_tareas.domain.model.OrdenTrabajo

data class OrdenTecnicoUiState(
    val ordenes: List<OrdenTrabajo> = emptyList(),
    val isLoading: Boolean = false,

    val filtroEstado: String = "Todas",
    val searchQuery: String = "",

    val nombreTecnico: String = "Usuario (Técnico)",
    val fechaActual: String = ""
)
