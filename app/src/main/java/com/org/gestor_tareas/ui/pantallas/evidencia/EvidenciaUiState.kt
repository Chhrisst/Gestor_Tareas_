package com.org.gestor_tareas.ui.pantallas.evidencia

import android.net.Uri
import java.io.File

data class EvidenciaUiState(
    val fotoUri: Uri? = null,
    val archivoLocal: File? = null,
    val isUploading: Boolean = false,
    val isUploaded: Boolean = false
)
