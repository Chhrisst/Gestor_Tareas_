package com.org.gestor_tareas.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccountPendingScreen(
    onCerrarSesionClick: () -> Unit
) {
    val azulFondo = Color(0xFF0D1B2A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(azulFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.HourglassEmpty,
            contentDescription = "Reloj de arena",
            tint = Color.White,
            modifier = Modifier.size(100.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "¡Cuenta creada con éxito!",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Tu cuenta está en estado PENDIENTE.\n\nContacta al administrador para habilitar las funciones de la aplicación.",
            color = Color.LightGray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onCerrarSesionClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = azulFondo),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "CERRAR SESIÓN", fontWeight = FontWeight.Bold)
        }
    }
}
