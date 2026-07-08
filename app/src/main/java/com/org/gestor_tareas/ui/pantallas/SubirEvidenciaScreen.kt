package com.org.gestor_tareas.ui.pantallas

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.org.gestor_tareas.data.util.FileUtils
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import com.org.gestor_tareas.ui.pantallas.evidencia.EvidenciaViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubirEvidenciaScreen(
    navController: NavController,
    viewModel: EvidenciaViewModel,
    tareaId: String
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var archivoTemp by remember { mutableStateOf<File?>(null) }
    var uriTemp by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && uriTemp != null && archivoTemp != null) {
            viewModel.onFotoCapturada(uriTemp!!, archivoTemp!!)
        }
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val archivo = FileUtils.crearArchivoTemporal(context)
            val uri = FileUtils.obtenerUriParaArchivo(context, archivo)
            archivoTemp = archivo
            uriTemp = uri
            cameraLauncher.launch(uri)
        }
    }

    fun abrirCamara() {
        val permisoConcedido = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (permisoConcedido) {
            val archivo = FileUtils.crearArchivoTemporal(context)
            val uri = FileUtils.obtenerUriParaArchivo(context, archivo)
            archivoTemp = archivo
            uriTemp = uri
            cameraLauncher.launch(uri)
        } else {
            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(Unit) {
        EventBus.events.collect { event ->
            when (event) {
                is UiEvent.Success -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.Error -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.Warning -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Subir Evidencia") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .clickable { abrirCamara() },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.fotoUri != null) {
                    val bitmap = remember(uiState.fotoUri) {
                        context.contentResolver.openInputStream(uiState.fotoUri!!)?.use {
                            BitmapFactory.decodeStream(it)
                        }
                    }
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Evidencia capturada",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Tomar foto",
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.subirEvidencia(tareaId) {
                        navController.popBackStack()
                    }
                },
                enabled = uiState.fotoUri != null && !uiState.isUploading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState.isUploading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Text("Subir Evidencia")
                }
            }
        }
    }
}
