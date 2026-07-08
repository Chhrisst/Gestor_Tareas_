package com.org.gestor_tareas.ui.pantallas.evidencia

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.usecase.SubirEvidenciaUseCase
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class EvidenciaViewModel(
    private val subirEvidenciaUseCase: SubirEvidenciaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EvidenciaUiState())
    val uiState = _uiState.asStateFlow()

    fun onFotoCapturada(uri: Uri, archivo: File) {
        _uiState.value = _uiState.value.copy(fotoUri = uri, archivoLocal = archivo, isUploaded = false)
    }

    fun subirEvidencia(tareaId: String, onSubidaExitosa: () -> Unit) {
        val archivo = _uiState.value.archivoLocal
        if (archivo == null) {
            viewModelScope.launch { EventBus.send(UiEvent.Warning("Primero toma una foto")) }
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploading = true)
            try {
                subirEvidenciaUseCase(tareaId, archivo)
                _uiState.value = _uiState.value.copy(isUploading = false, isUploaded = true)
                EventBus.send(UiEvent.Success("Evidencia subida correctamente"))
                onSubidaExitosa()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isUploading = false)
                EventBus.send(UiEvent.Error(e.message ?: "Error al subir evidencia"))
            }
        }
    }
}
