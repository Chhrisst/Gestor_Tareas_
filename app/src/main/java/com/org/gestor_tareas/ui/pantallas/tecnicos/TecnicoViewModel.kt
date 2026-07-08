package com.org.gestor_tareas.ui.pantallas.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.model.Tecnico
import com.org.gestor_tareas.domain.usecase.TecnicoUseCases
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TecnicoViewModel(
    private val useCases: TecnicoUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(TecnicoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarTecnicos()
    }

    private fun cargarTecnicos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val lista = useCases.getTecnicos()
                _uiState.value = _uiState.value.copy(tecnicos = lista, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                EventBus.send(UiEvent.Error("Error al cargar técnicos: ${e.message}"))
            }
        }
    }

    fun onNombreChange(nombre: String) {
        _uiState.value = _uiState.value.copy(nombreInput = nombre)
    }

    fun onRolChange(rol: String) {
        _uiState.value = _uiState.value.copy(rolInput = rol)
    }

    fun abrirDialogoNuevo() {
        _uiState.value = _uiState.value.copy(
            mostrarDialogo = true,
            tecnicoEditandoId = null,
            nombreInput = "",
            rolInput = "Técnico"
        )
    }

    fun abrirDialogoEditar(tecnico: Tecnico) {
        _uiState.value = _uiState.value.copy(
            mostrarDialogo = true,
            tecnicoEditandoId = tecnico.id,
            nombreInput = tecnico.nombre,
            rolInput = tecnico.rol
        )
    }

    fun cerrarDialogo() {
        _uiState.value = _uiState.value.copy(mostrarDialogo = false)
    }

    fun guardarTecnico() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.nombreInput.isBlank() || state.rolInput.isBlank()) {
                EventBus.send(UiEvent.Warning("Por favor completa todos los campos"))
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true, mostrarDialogo = false)
            try {
                if (state.tecnicoEditandoId == null) {
                    useCases.addTecnico(state.nombreInput, state.rolInput)
                    EventBus.send(UiEvent.Success("Técnico agregado con éxito"))
                } else {
                    useCases.updateTecnico(state.tecnicoEditandoId, state.nombreInput, state.rolInput)
                    EventBus.send(UiEvent.Success("Técnico actualizado"))
                }
                cargarTecnicos()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                EventBus.send(UiEvent.Error("Error al guardar: ${e.message}"))
            }
        }
    }

    fun eliminarTecnico(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                useCases.deleteTecnico(id)
                EventBus.send(UiEvent.Success("Técnico eliminado"))
                cargarTecnicos()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                EventBus.send(UiEvent.Error("Error al eliminar: ${e.message}"))
            }
        }
    }
}