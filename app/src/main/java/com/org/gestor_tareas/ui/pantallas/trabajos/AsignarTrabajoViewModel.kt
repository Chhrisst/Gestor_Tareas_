package com.org.gestor_tareas.ui.pantallas.trabajos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.model.Tecnico
import com.org.gestor_tareas.domain.usecase.TecnicoUseCases
import com.org.gestor_tareas.domain.usecase.TrabajoUseCases
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AsignarTrabajoViewModel(
    private val tecnicoUseCases: TecnicoUseCases,
    private val trabajoUseCases: TrabajoUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AsignarTrabajoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarTecnicos()
    }

    fun cargarTecnicos() {
        viewModelScope.launch {
            try {
                val lista = tecnicoUseCases.getTecnicos()
                _uiState.value = _uiState.value.copy(tecnicos = lista)
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al cargar técnicos: ${e.message}"))
            }
        }
    }

    fun onNombreClienteChange(valor: String) {
        _uiState.value = _uiState.value.copy(nombreClienteInput = valor)
    }

    fun onServicioChange(valor: String) {
        _uiState.value = _uiState.value.copy(servicioInput = valor)
    }

    fun onDireccionChange(valor: String) {
        _uiState.value = _uiState.value.copy(direccionInput = valor)
    }

    fun onFechaChange(valor: String) {
        _uiState.value = _uiState.value.copy(fechaInput = valor)
    }

    fun onHorarioChange(valor: String) {
        _uiState.value = _uiState.value.copy(horarioInput = valor)
    }

    fun onTecnicoSeleccionado(tecnico: Tecnico) {
        _uiState.value = _uiState.value.copy(tecnicoSeleccionado = tecnico)
    }

    fun guardarTrabajo(onExito: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value

            if (state.nombreClienteInput.isBlank() || state.servicioInput.isBlank() || state.direccionInput.isBlank() || state.fechaInput.isBlank() || state.horarioInput.isBlank()) {
                EventBus.send(UiEvent.Warning("Por favor completa todos los campos"))
                return@launch
            }
            if (state.tecnicoSeleccionado == null) {
                EventBus.send(UiEvent.Warning("Selecciona un técnico"))
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                trabajoUseCases.addTrabajo(
                    state.nombreClienteInput,
                    state.servicioInput,
                    state.direccionInput,
                    state.fechaInput,
                    state.horarioInput,
                    state.tecnicoSeleccionado.id,
                    state.tecnicoSeleccionado.nombre
                )
                EventBus.send(UiEvent.Success("Trabajo asignado con éxito"))
                _uiState.value = AsignarTrabajoUiState(tecnicos = state.tecnicos)
                onExito()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                EventBus.send(UiEvent.Error("Error al asignar el trabajo: ${e.message}"))
            }
        }
    }
}
