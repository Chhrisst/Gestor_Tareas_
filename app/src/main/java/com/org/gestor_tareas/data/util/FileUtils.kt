package com.org.gestor_tareas.data.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object FileUtils {

    fun crearArchivoTemporal(context: Context): File {
        val nombre = "EVIDENCIA_${System.currentTimeMillis()}"
        return File.createTempFile(nombre, ".jpg", context.cacheDir)
    }

    fun obtenerUriParaArchivo(context: Context, archivo: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )
    }
}
