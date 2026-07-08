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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdenTecnicoViewModel(
    private val useCases: OrdenUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdenTecnicoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Establecer fecha inicial
        val formato = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("es", "ES"))
        val fecha = formato.format(Date()).replaceFirstChar { it.uppercase() }
        _uiState.update { it.copy(fechaActual = fecha) }
        
        cargarOrdenes()
    }

    fun cargarOrdenes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val lista = useCases.getOrdenes()
                _uiState.update { it.copy(ordenes = lista, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                EventBus.send(UiEvent.Error("Error al cargar órdenes: ${e.message}"))
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onFiltroChange(filtro: String) {
        _uiState.update { it.copy(filtroEstado = filtro) }
    }

    fun actualizarEstadoOrden(id: Int, nuevoEstado: String) {
        viewModelScope.launch {
            val orden = _uiState.value.ordenes.find { it.id == id } ?: return@launch
            try {
                useCases.updateOrden(
                    id = id,
                    servicio = orden.servicio,
                    cliente = orden.clienteNombre,
                    direccion = orden.direccion,
                    hora = orden.horaAtencion,
                    fecha = orden.fecha,
                    estado = nuevoEstado
                )
                EventBus.send(UiEvent.Success("Estado actualizado a $nuevoEstado"))
                cargarOrdenes()
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al actualizar estado: ${e.message}"))
            }
        }
    }
}
