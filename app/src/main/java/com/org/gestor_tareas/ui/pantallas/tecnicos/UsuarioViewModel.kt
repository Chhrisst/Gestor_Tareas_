package com.org.gestor_tareas.ui.pantallas.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.usecase.UsuarioUseCases
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val useCases: UsuarioUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState = _uiState.asStateFlow()

    init { cargarUsuarios() }

    fun cargarUsuarios() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val lista = useCases.listarUsuarios()
                _uiState.value = _uiState.value.copy(usuarios = lista, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                EventBus.send(UiEvent.Error("Error al cargar usuarios: ${e.message}"))
            }
        }
    }

    fun onNombreChange(v: String) { _uiState.value = _uiState.value.copy(nombreInput = v) }
    fun onEmailChange(v: String) { _uiState.value = _uiState.value.copy(emailInput = v) }
    fun onPasswordChange(v: String) { _uiState.value = _uiState.value.copy(passwordInput = v) }
    fun onRolChange(v: String) { _uiState.value = _uiState.value.copy(rolInput = v) }

    fun abrirDialogoNuevo() {
        _uiState.value = _uiState.value.copy(
            mostrarDialogo = true,
            usuarioEditandoEmail = null,
            nombreInput = "",
            emailInput = "",
            passwordInput = "",
            rolInput = "Técnico"
        )
    }

    fun cerrarDialogo() {
        _uiState.value = _uiState.value.copy(mostrarDialogo = false)
    }

    fun guardarUsuario() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.nombreInput.isBlank() || state.emailInput.isBlank() || state.passwordInput.isBlank()) {
                EventBus.send(UiEvent.Warning("Por favor completa todos los campos"))
                return@launch
            }
            _uiState.value = _uiState.value.copy(isLoading = true, mostrarDialogo = false)
            try {
                useCases.registrarUsuario(state.nombreInput, state.emailInput, state.passwordInput, state.rolInput)
                EventBus.send(UiEvent.Success("Usuario creado con éxito"))
                cargarUsuarios()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                EventBus.send(UiEvent.Error("Error al guardar: ${e.message}"))
            }
        }
    }

    fun eliminarUsuario(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                useCases.eliminarUsuario(email)
                EventBus.send(UiEvent.Success("Usuario eliminado"))
                cargarUsuarios()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                EventBus.send(UiEvent.Error("Error al eliminar: ${e.message}"))
            }
        }
    }
}
