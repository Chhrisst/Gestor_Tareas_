package com.org.gestor_tareas.ui.pantallas.tecnicos

import com.org.gestor_tareas.domain.model.Tecnico

data class TecnicoUiState(
    val tecnicos: List<Tecnico> = emptyList(),
    val isLoading: Boolean = false,
    val mostrarDialogo: Boolean = false,
    val tecnicoEditandoId: Int? = null,
    val nombreInput: String = "",
    val rolInput: String = "Técnico"
)