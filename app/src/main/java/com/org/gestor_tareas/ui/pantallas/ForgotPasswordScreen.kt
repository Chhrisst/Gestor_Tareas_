package com.org.gestor_tareas.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen(
    onEnviarClick: (email: String) -> Unit = {},
    onVolverClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulOscuro)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Recuperar contraseña",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 26.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp, vertical = 40.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        Text(
                            text = "Ingresa tu correo electrónico registrado y te enviaremos las instrucciones para restablecer tu contraseña.",
                            color = GrisTexto,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Justify
                        )

                        CampoUnderline(
                            label       = "Correo Electrónico",
                            placeholder = "cody@email.com",
                            value       = email,
                            onValueChange = { email = it },
                            keyboardType  = KeyboardType.Email
                        )

                        Button(
                            onClick  = { onEnviarClick(email) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape  = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = AzulBoton)
                        ) {
                            Text(
                                text       = "ENVIAR INSTRUCCIONES",
                                color      = Color.White,
                                fontSize   = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    TextButton(
                        onClick  = onVolverClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("¿Recordaste tu contraseña?\n")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = GrisTexto)) {
                                    append("Inicia sesión")
                                }
                            },
                            textAlign = TextAlign.Center,
                            color     = GrisTexto,
                            fontSize  = 14.sp
                        )
                    }
                }
            }
        }
    }
}