package com.org.gestor_tareas.ui.pantallas.ordenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.usecase.ObtenerEstadoOrdenesUseCase
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrdenesViewModel(
    private val obtenerEstadoOrdenesUseCase: ObtenerEstadoOrdenesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdenesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarEstadoOrdenes()
    }

    private fun cargarEstadoOrdenes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val estado = obtenerEstadoOrdenesUseCase()
                _uiState.value = _uiState.value.copy(estado = estado, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                EventBus.send(UiEvent.Error("Error al cargar las órdenes: ${e.message}"))
            }
        }
    }
}
