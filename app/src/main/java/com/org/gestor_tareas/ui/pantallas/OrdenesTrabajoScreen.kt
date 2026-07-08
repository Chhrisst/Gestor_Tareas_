package com.org.gestor_tareas.ui.pantallas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.org.gestor_tareas.domain.model.EstadoOrdenes
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import com.org.gestor_tareas.ui.pantallas.ordenes.OrdenesViewModel

private val VerdeEstado = Color(0xFF35D07F)
private val AmarilloEstado = Color(0xFFF2C84B)
private val RojoEstado = Color(0xFFEF5350)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenesTrabajoScreen(
    navController: NavController,
    viewModel: OrdenesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
                title = { Text("Órdenes de trabajo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        if (uiState.isLoading || uiState.estado == null) {
            Box(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val estado = uiState.estado!!

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Estado de las Órdenes de trabajo",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            DonaEstadoOrdenes(estado = estado)

            Spacer(modifier = Modifier.height(32.dp))

            LeyendaFila(
                color = VerdeEstado,
                texto = "Tareas realizadas con éxito",
                porcentaje = estado.porcentajeFinalizadas
            )
            LeyendaFila(
                color = AmarilloEstado,
                texto = "Tareas en proceso",
                porcentaje = estado.porcentajeEnProceso
            )
            LeyendaFila(
                color = RojoEstado,
                texto = "Tareas pendientes",
                porcentaje = estado.porcentajePendientes
            )
        }
    }
}

@Composable
private fun DonaEstadoOrdenes(estado: EstadoOrdenes) {
    val grosorAnillo = 34.dp

    Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = grosorAnillo.toPx(), cap = StrokeCap.Butt)
            val diametro = size.minDimension - stroke.width
            val tamañoArco = Size(diametro, diametro)
            val offset = androidx.compose.ui.geometry.Offset(
                (size.width - diametro) / 2f,
                (size.height - diametro) / 2f
            )

            var anguloInicio = -90f

            val segmentos = listOf(
                estado.porcentajePendientes to RojoEstado,
                estado.porcentajeEnProceso to AmarilloEstado,
                estado.porcentajeFinalizadas to VerdeEstado
            )

            segmentos.forEach { (porcentaje, color) ->
                val barrido = 360f * (porcentaje / 100f)
                drawArc(
                    color = color,
                    startAngle = anguloInicio,
                    sweepAngle = barrido,
                    useCenter = false,
                    topLeft = offset,
                    size = tamañoArco,
                    style = stroke
                )
                anguloInicio += barrido
            }
        }

        Text(
            text = "${estado.totalTareas}\nTareas",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun LeyendaFila(color: Color, texto: String, porcentaje: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = texto, fontSize = 15.sp)
            Text(text = "$porcentaje%", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}
