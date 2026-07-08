package com.org.gestor_tareas.ui.pantallas.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.usecase.OrdenUseCases
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AsignarOrdenViewModel(
    private val useCases: OrdenUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AsignarOrdenUiState())
    val uiState = _uiState.asStateFlow()

    fun onTecnicoChange(value: String) { _uiState.update { it.copy(nombreTecnico = value) } }
    fun onServicioChange(value: String) { _uiState.update { it.copy(servicio = value) } }
    fun onDireccionChange(value: String) { _uiState.update { it.copy(direccion = value) } }
    fun onClienteChange(value: String) { _uiState.update { it.copy(cliente = value) } }
    fun onHoraChange(value: String) { _uiState.update { it.copy(hora = value) } }
    fun onFechaChange(value: String) { _uiState.update { it.copy(fecha = value) } }

    fun guardarOrden(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.servicio.isBlank() || state.cliente.isBlank() || state.direccion.isBlank()) {
                EventBus.send(UiEvent.Warning("Por favor completa los campos obligatorios"))
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }
            try {
                useCases.addOrden(
                    servicio = state.servicio,
                    cliente = state.cliente,
                    direccion = state.direccion,
                    hora = state.hora,
                    fecha = state.fecha,
                    estado = "Pendiente"
                )
                EventBus.send(UiEvent.Success("Orden asignada con éxito"))
                resetForm()
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                EventBus.send(UiEvent.Error("Error al asignar orden: ${e.message}"))
            }
        }
    }

    private fun resetForm() {
        _uiState.update { AsignarOrdenUiState() }
    }
}

data class AsignarOrdenUiState(
    val nombreTecnico: String = "",
    val servicio: String = "",
    val direccion: String = "",
    val cliente: String = "",
    val hora: String = "08:00 AM",
    val fecha: String = "",
    val isLoading: Boolean = false
)
