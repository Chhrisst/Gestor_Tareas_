package com.org.gestor_tareas.ui.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.gestor_tareas.domain.model.Tecnico
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import com.org.gestor_tareas.ui.pantallas.trabajos.AsignarTrabajoViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val colorBotonCyan = Color(0xFF1396A9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignarTrabajoScreen(
    viewModel: AsignarTrabajoViewModel,
    onVolverClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.cargarTecnicos()
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
        topBar = {
            TopAppBar(
                title = { Text("Asignar Trabajo", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            CampoFormulario(titulo = "Nombre del Cliente", valor = uiState.nombreClienteInput, onValorCambia = viewModel::onNombreClienteChange)
            CampoFormulario(titulo = "Servicio", valor = uiState.servicioInput, onValorCambia = viewModel::onServicioChange)
            CampoFormulario(
                titulo = "Dirección",
                valor = uiState.direccionInput,
                onValorCambia = viewModel::onDireccionChange,
                icono = Icons.Default.LocationOn
            )
            SelectorFecha(valor = uiState.fechaInput, onFechaSeleccionada = viewModel::onFechaChange)
            SelectorHorario(valor = uiState.horarioInput, onHorarioSeleccionado = viewModel::onHorarioChange)

            SelectorTecnico(
                tecnicos = uiState.tecnicos,
                tecnicoSeleccionado = uiState.tecnicoSeleccionado,
                onTecnicoSeleccionado = viewModel::onTecnicoSeleccionado
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.guardarTrabajo(onExito = onVolverClick) },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorBotonCyan)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.height(24.dp))
                } else {
                    Text(text = "Agregar", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorTecnico(
    tecnicos: List<Tecnico>,
    tecnicoSeleccionado: Tecnico?,
    onTecnicoSeleccionado: (Tecnico) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = "Seleccionar técnico", fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(bottom = 6.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = tecnicoSeleccionado?.nombre ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorBotonCyan,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (tecnicos.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No hay técnicos disponibles") },
                        onClick = { expanded = false }
                    )
                }

                tecnicos.forEach { tecnico ->
                    DropdownMenuItem(
                        text = { Text(tecnico.nombre) },
                        onClick = {
                            onTecnicoSeleccionado(tecnico)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CampoFormulario(
    titulo: String,
    valor: String,
    onValorCambia: (String) -> Unit,
    maxLines: Int = 1,
    icono: androidx.compose.ui.graphics.vector.ImageVector? = null
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
            trailingIcon = if (icono != null) {
                { Icon(icono, contentDescription = null, tint = Color.Black) }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorBotonCyan,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = Color(0xFFF9F9F9),
                unfocusedContainerColor = Color(0xFFF9F9F9),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorFecha(
    valor: String,
    onFechaSeleccionada: (String) -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column {
        Text(text = "Fecha", fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(bottom = 6.dp))
        Box {
            OutlinedTextField(
                value = valor,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorBotonCyan,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { mostrarDialogo = true }
            )
        }
    }

    if (mostrarDialogo) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formato = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))
                        onFechaSeleccionada(formato.format(Date(millis)))
                    }
                    mostrarDialogo = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorHorario(
    valor: String,
    onHorarioSeleccionado: (String) -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column {
        Text(text = "Horario atención", fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(bottom = 6.dp))
        Box {
            OutlinedTextField(
                value = valor,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorBotonCyan,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { mostrarDialogo = true }
            )
        }
    }

    if (mostrarDialogo) {
        val calendar = Calendar.getInstance()
        val timePickerState = rememberTimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = calendar.get(Calendar.MINUTE),
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Selecciona la hora") },
            text = { TimePicker(state = timePickerState) },
            confirmButton = {
                TextButton(onClick = {
                    val horaTexto = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    onHorarioSeleccionado(horaTexto)
                    mostrarDialogo = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            },
            containerColor = Color.White
        )
    }
}
