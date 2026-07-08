package com.org.gestor_tareas.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.org.gestor_tareas.domain.model.Usuario
import com.org.gestor_tareas.ui.event.EventBus
import com.org.gestor_tareas.ui.event.UiEvent
import com.org.gestor_tareas.ui.pantallas.tecnicos.UsuarioViewModel

private val AzulTecnico = Color(0xFF00BCD4)
private val FondoItem   = Color(0xFFE0F7FA)

// Opciones de rol disponibles en el combobox
private val ROLES_DISPONIBLES = listOf("Administrador", "Técnico", "Pendiente", "Deshabilitado")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoListScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
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
                title = { Text(text = "Gestión de Usuarios", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.abrirDialogoNuevo() },
                containerColor = AzulTecnico,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo usuario", tint = Color.White)
            }
        },
        containerColor = Color.White
    ) { padding ->

        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.usuarios) { usuario ->
                    UsuarioItem(
                        usuario = usuario,
                        onEliminar = { viewModel.eliminarUsuario(usuario.email) }
                    )
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AzulTecnico
                )
            }
        }

        if (uiState.mostrarDialogo) {
            NuevoUsuarioDialog(
                uiState = uiState,
                onNombreChange = viewModel::onNombreChange,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onRolChange = viewModel::onRolChange,
                onGuardar = { viewModel.guardarUsuario() },
                onCancelar = { viewModel.cerrarDialogo() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NuevoUsuarioDialog(
    uiState: com.org.gestor_tareas.ui.pantallas.tecnicos.UsuarioUiState,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    var mostrarPassword by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(text = "Nuevo Usuario", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = uiState.nombreInput,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.emailInput,
                    onValueChange = onEmailChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.passwordInput,
                    onValueChange = onPasswordChange,
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                            Icon(
                                imageVector = if (mostrarPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (mostrarPassword) "Ocultar" else "Mostrar"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                RolDropdown(
                    rolSeleccionado = uiState.rolInput,
                    onRolSeleccionado = onRolChange
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onGuardar,
                colors = ButtonDefaults.buttonColors(containerColor = AzulTecnico)
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun UsuarioItem(
    usuario: Usuario,
    onEliminar: () -> Unit
) {
    val colorRol = when (usuario.rol.uppercase()) {
        "ADMIN" -> Color(0xFF1565C0)
        "TECNICO" -> AzulTecnico
        "PENDIENTE" -> Color(0xFFF57F17)
        "INACTIVO" -> Color(0xFFB71C1C)
        else -> Color.Gray
    }
    val rolMostrado = when (usuario.rol.uppercase()) {
        "ADMIN" -> "Administrador"
        "TECNICO" -> "Técnico"
        "PENDIENTE" -> "Pendiente"
        "INACTIVO" -> "Deshabilitado"
        else -> usuario.rol
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoItem),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB2EBF2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = AzulTecnico, modifier = Modifier.size(30.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = usuario.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AzulTecnico)
                Text(text = usuario.email, fontSize = 11.sp, color = Color.Gray)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorRol.copy(alpha = 0.12f),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = rolMostrado,
                        fontSize = 11.sp,
                        color = colorRol,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolDropdown(
    rolSeleccionado: String,
    onRolSeleccionado: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = rolSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text("Rol / Cargo") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            ROLES_DISPONIBLES.forEach { rol ->
                DropdownMenuItem(
                    text = { Text(rol) },
                    onClick = {
                        onRolSeleccionado(rol)
                        expandido = false
                    }
                )
            }
        }
    }
}