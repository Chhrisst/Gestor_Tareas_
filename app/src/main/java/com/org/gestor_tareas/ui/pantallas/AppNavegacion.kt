package com.org.gestor_tareas.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.gestor_tareas.navigation.Rutas
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AppNavegacion(container: com.org.gestor_tareas.di.AppContainer) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var isLoading by remember { mutableStateOf(true) }
    var startDestination by remember { mutableStateOf(Rutas.Inicio.ruta) }
    var userRol by remember { mutableStateOf("") }
    var userNombre by remember { mutableStateOf("") }

    // Función para refrescar y validar la sesión con el servidor de forma dinámica
    val checkSession = suspend {
        val token = container.tokenDataStore.token.first()
        val savedEmail = container.tokenDataStore.email.first() ?: ""
        val savedRol = container.tokenDataStore.rol.first() ?: ""
        val savedNombre = container.tokenDataStore.nombre.first() ?: ""
        
        if (!token.isNullOrEmpty() && savedEmail.isNotEmpty()) {
            try {
                // Consultamos el estado REAL en el servidor usando el email del usuario logueado
                val response = container.repositoryModule.authRepository.verificarSesion(savedEmail)
                
                if (response.isSuccessful && response.body() != null) {
                    val serverData = response.body()!!.data
                    userRol = serverData.rol
                    userNombre = serverData.nombre
                    
                    // Sincronizamos localmente solo si hubo cambios en el servidor
                    if (serverData.rol != savedRol || serverData.nombre != savedNombre) {
                        container.tokenDataStore.saveAuthData(token, serverData.rol, serverData.nombre, savedEmail)
                    }
                    
                    // Determinamos destino según el rol actualizado del servidor
                    startDestination = when (serverData.rol) {
                        "PENDIENTE", "INACTIVO" -> Rutas.Espera.ruta
                        else -> Rutas.Dashboard.ruta
                    }
                } else {
                    // Si el servidor responde error (ej: usuario borrado), usamos datos locales por seguridad de acceso offline
                    userRol = savedRol
                    userNombre = savedNombre
                    startDestination = if (savedRol == "PENDIENTE" || savedRol == "INACTIVO") Rutas.Espera.ruta else Rutas.Dashboard.ruta
                }
            } catch (e: Exception) {
                // Error de red: confiamos en lo que ya estaba guardado
                userRol = savedRol
                userNombre = savedNombre
                startDestination = if (savedRol == "PENDIENTE" || savedRol == "INACTIVO") Rutas.Espera.ruta else Rutas.Dashboard.ruta
            }
        }
        isLoading = false
    }

    LaunchedEffect(Unit) {
        checkSession()
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(Rutas.Inicio.ruta) {
                InicioScreen(
                    loginViewModel = container.loginViewModel,
                    onIniciarSesionClick = { navController.navigate(Rutas.Login.ruta) },
                    onRegistrarseClick = { navController.navigate(Rutas.Registro.ruta) },
                    onGoogleLoginSuccess = { rol, nombre ->
                        userRol = rol
                        userNombre = nombre
                        if (rol == "PENDIENTE" || rol == "INACTIVO") {
                            navController.navigate(Rutas.Espera.ruta) { popUpTo(Rutas.Inicio.ruta) { inclusive = true } }
                        } else {
                            navController.navigate(Rutas.Dashboard.ruta) { popUpTo(Rutas.Inicio.ruta) { inclusive = true } }
                        }
                    }
                )
            }

            composable(Rutas.Login.ruta) {
                LoginScreen(
                    viewModel = container.loginViewModel,
                    onLoginSuccess = { rol, nombre ->
                        userRol = rol
                        userNombre = nombre
                        if (rol == "PENDIENTE" || rol == "INACTIVO") {
                            navController.navigate(Rutas.Espera.ruta) { popUpTo(Rutas.Inicio.ruta) { inclusive = true } }
                        } else {
                            navController.navigate(Rutas.Dashboard.ruta) { popUpTo(Rutas.Inicio.ruta) { inclusive = true } }
                        }
                    },
                    onRegistrateClick = { navController.navigate(Rutas.Registro.ruta) },
                    onOlvidasteClick = { navController.navigate(Rutas.Olvidaste.ruta) }
                )
            }

            composable(Rutas.Registro.ruta) {
                RegisterScreen(
                    viewModel = container.registerViewModel,
                    onRegisterSuccess = { rol, nombre ->
                        userRol = rol
                        userNombre = nombre
                        Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_LONG).show()
                        navController.navigate(Rutas.Espera.ruta) { popUpTo(Rutas.Inicio.ruta) { inclusive = true } }
                    },
                    onIniciarSesionClick = { navController.navigate(Rutas.Login.ruta) }
                )
            }
            
            composable(Rutas.Espera.ruta) {
                AccountPendingScreen(onCerrarSesionClick = {
                    scope.launch {
                        container.tokenDataStore.clearAll()
                        navController.navigate(Rutas.Inicio.ruta) { popUpTo(0) { inclusive = true } }
                    }
                })
            }

            composable(Rutas.Olvidaste.ruta) {
                ForgotPasswordScreen(
                    onEnviarClick = { email ->
                        Toast.makeText(context, "Instrucciones enviadas a: $email", Toast.LENGTH_LONG).show()
                        navController.navigate(Rutas.Login.ruta) { popUpTo(Rutas.Login.ruta) { inclusive = true } }
                    },
                    onVolverClick = { navController.popBackStack() }
                )
            }

            composable(Rutas.Dashboard.ruta) {
                DashboardScreen(
                    viewModel = container.dashboardViewModel,
                    nombreAdministrador = userNombre, 
                    rolUsuario = userRol,
                    onVerTecnicosClick = { navController.navigate("lista_tecnicos") },
                    onVerOrdenesClick = { 
                        if (userRol == "TECNICO") {
                            navController.navigate("inicio_tecnico")
                        } else {
                            navController.navigate(Rutas.OrdenesTrabajo.ruta)
                        }
                    },
                    onDelegarTareasClick = { navController.navigate(Rutas.AsignarTrabajo.ruta) },
                    onCerrarSesionClick = {
                        scope.launch {
                            container.tokenDataStore.clearAll()
                            navController.navigate(Rutas.Inicio.ruta) { popUpTo(0) { inclusive = true } }
                        }
                    }
                )
            }

            composable("inicio_tecnico") {
                com.org.gestor_tareas.ui.pantallas.tecnicos.InicioTecnicoScreen(
                    viewModel = container.ordenTecnicoViewModel,
                    onCerrarSesionClick = {
                        scope.launch {
                            container.tokenDataStore.clearAll()
                            navController.navigate(Rutas.Inicio.ruta) { popUpTo(0) { inclusive = true } }
                        }
                    }
                )
            }

            composable(Rutas.AsignarOrden.ruta) { 
                AsignarOrdenScreen(
                    onVolverClick = { navController.popBackStack() },
                    viewModel = container.asignarOrdenViewModel
                ) 
            }
            composable(Rutas.AsignarTrabajo.ruta) { 
                AsignarTrabajoScreen(
                    viewModel = container.asignarTrabajoViewModel,
                    onVolverClick = { navController.popBackStack() }
                ) 
            }
            composable("lista_tecnicos") { TecnicoListScreen(navController = navController, viewModel = container.usuarioViewModel) }
            
            composable(Rutas.OrdenesTrabajo.ruta) {
                OrdenesTrabajoScreen(
                    navController = navController,
                    viewModel = container.ordenesViewModel
                )
            }
            composable(Rutas.EjecucionTarea.ruta) { backStackEntry ->
                val ordenId = backStackEntry.arguments?.getString("ordenId") ?: ""
                EjecucionTareaScreen(
                    navController = navController,
                    viewModel = container.tareaViewModel,
                    tareaId = ordenId
                )
            }
            composable(Rutas.SubirEvidencia.ruta) { backStackEntry ->
                val tareaId = backStackEntry.arguments?.getString("tareaId") ?: ""
                SubirEvidenciaScreen(
                    navController = navController,
                    viewModel = container.evidenciaViewModel,
                    tareaId = tareaId
                )
            }
        }
    }
}
