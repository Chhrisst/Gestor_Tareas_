package com.org.gestor_tareas.ui.pantallas.trabajos

import com.org.gestor_tareas.domain.model.Tecnico

data class AsignarTrabajoUiState(
    val tecnicos: List<Tecnico> = emptyList(),
    val tecnicoSeleccionado: Tecnico? = null,
    val nombreClienteInput: String = "",
    val servicioInput: String = "",
    val direccionInput: String = "",
    val fechaInput: String = "",
    val horarioInput: String = "",
    val isLoading: Boolean = false
)
