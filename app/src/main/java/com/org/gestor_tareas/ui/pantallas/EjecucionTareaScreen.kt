package com.org.gestor_tareas.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import com.org.gestor_tareas.ui.pantallas.tareas.TareaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EjecucionTareaScreen(
    navController: NavController,
    viewModel: TareaViewModel,
    tareaId: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Carga la tarea la primera vez que se entra a la pantalla
    LaunchedEffect(tareaId) {
        viewModel.cargarTarea(tareaId)
    }

    // Mismo patrón de EventBus que TecnicoListScreen
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
        topBar = { TopAppBar(title = { Text("Ejecución de Tarea") }) }
    ) { padding ->
        if (uiState.isLoading || uiState.tarea == null) {
            Box(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val tarea = uiState.tarea!!

        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
                    .background(Color(0xFFB0BEC5)),
                contentAlignment = Alignment.Center
            ) {
                Text("Aquí va el mapa (Google Maps Compose)", color = Color.DarkGray)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f)
                    .background(Color(0xFF10162B))
                    .padding(20.dp)
            ) {
                if (tarea.monitoreoAwsActivo) {
                    Row(
                        modifier = Modifier
                            .background(Color(0xFF6C4FE0), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Monitoreo AWS Activado",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Estado", color = Color.LightGray, style = MaterialTheme.typography.labelSmall)
                        Text(
                            tarea.estado,
                            color = if (tarea.estado == "Pendiente") Color(0xFFFFC107) else Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Distancia", color = Color.LightGray, style = MaterialTheme.typography.labelSmall)
                        Text("${tarea.distanciaKm} KM", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("#${tarea.id}", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Cliente: ${tarea.cliente}", color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
                Text("Dirección: ${tarea.direccion}", color = Color.LightGray, style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navController.navigate("subir_evidencia/${tarea.id}") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SUBIR EVIDENCIA")
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { viewModel.iniciarTrabajoEnSitio(tarea.id) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = tarea.estado == "Pendiente"
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (tarea.estado == "Pendiente") "INICIAR TRABAJO EN SITIO" else "TRABAJO EN PROGRESO")
                }
            }
        }
    }
}
