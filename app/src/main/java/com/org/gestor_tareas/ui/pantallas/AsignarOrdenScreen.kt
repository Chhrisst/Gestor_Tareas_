package com.org.gestor_tareas.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.gestor_tareas.ui.pantallas.tecnicos.AsignarOrdenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignarOrdenScreen(
    onVolverClick: () -> Unit,
    viewModel: AsignarOrdenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val colorBotonCyan = Color(0xFF1396A9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asignar Orden", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            CampoFormulario(titulo = "Nombre del Tecnico", valor = uiState.nombreTecnico, onValorCambia = viewModel::onTecnicoChange)
            CampoFormulario(titulo = "Detalle del Servicio", valor = uiState.servicio, onValorCambia = viewModel::onServicioChange, maxLines = 3)
            CampoFormulario(titulo = "Dirección", valor = uiState.direccion, onValorCambia = viewModel::onDireccionChange)
            CampoFormulario(titulo = "Cliente", valor = uiState.cliente, onValorCambia = viewModel::onClienteChange)
            CampoFormulario(titulo = "Fecha", valor = uiState.fecha, onValorCambia = viewModel::onFechaChange)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.guardarOrden(onSuccess = {
                        // Opcional: navegar atrás o limpiar
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorBotonCyan),
                enabled = !uiState.isLoading
            ) {


                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Agregar", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CampoFormulario(
    titulo: String,
    valor: String,
    onValorCambia: (String) -> Unit,
    maxLines: Int = 1
) {
    Column {
        Text(text = titulo, fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(bottom = 6.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambia,
            modifier = Modifier.fillMaxWidth(),
            singleLine = maxLines == 1,
            maxLines = maxLines,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1396A9),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = Color(0xFFF9F9F9),
                unfocusedContainerColor = Color(0xFFF9F9F9)
            )
        )
    }
}
