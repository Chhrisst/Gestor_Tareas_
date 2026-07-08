package com.org.gestor_tareas.ui.pantallas.tareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.usecase.TareaUseCases
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TareaViewModel(
    private val useCases: TareaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(TareaUiState())
    val uiState = _uiState.asStateFlow()

    fun cargarTarea(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val tarea = useCases.obtenerTarea(id)
                _uiState.value = _uiState.value.copy(tarea = tarea, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                EventBus.send(UiEvent.Error("Error al cargar la tarea: ${e.message}"))
            }
        }
    }

    fun iniciarTrabajoEnSitio(id: String) {
        viewModelScope.launch {
            try {
                val actualizada = useCases.iniciarTrabajo(id)
                _uiState.value = _uiState.value.copy(tarea = actualizada)
                EventBus.send(UiEvent.Success("Trabajo iniciado en sitio"))
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al iniciar trabajo: ${e.message}"))
            }
        }
    }
}
