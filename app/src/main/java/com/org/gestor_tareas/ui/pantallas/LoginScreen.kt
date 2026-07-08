package com.org.gestor_tareas.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.gestor_tareas.ui.pantallas.auth.LoginViewModel

val AzulOscuro = Color(0xFF0D1B4B)
val AzulBoton  = Color(0xFF1A2E7A)
val GrisTexto  = Color(0xFF444444)

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String, String) -> Unit,
    onRegistrateClick: () -> Unit = {},
    onOlvidasteClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun validateFields(): Boolean {
        emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Formato de correo inválido" else null
        passwordError = if (password.isEmpty()) "Ingresa tu contraseña" else null
        return emailError == null && passwordError == null
    }

    Box(modifier = Modifier.fillMaxSize().background(AzulOscuro)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().weight(0.35f), contentAlignment = Alignment.Center) {
                Text(text = "Inicio de sesión", style = TextStyle(color = Color.White, fontSize = 26.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Normal))
            }

            Surface(
                modifier = Modifier.fillMaxWidth().weight(0.65f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 30.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        CampoUnderline(
                            label = "Correo Electrónico",
                            placeholder = "ejemplo@email.com",
                            value = email,
                            onValueChange = { email = it.trim(); emailError = null },
                            keyboardType = KeyboardType.Email,
                            maxLength = 128,
                            error = emailError
                        )

                        CampoUnderline(
                            label = "Contraseña",
                            placeholder = "••••••••",
                            value = password,
                            onValueChange = { password = it; passwordError = null },
                            esPassword = true,
                            maxLength = 128,
                            error = passwordError
                        )

                        if (uiState.error != null) {
                            Text(text = uiState.error!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        }

                        TextButton(onClick = onOlvidasteClick, modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(0.dp)) {
                            Text(text = "¿Olvidaste tu contraseña?", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, color = GrisTexto, fontSize = 13.sp)
                        }

                        Button(
                            onClick = { if (validateFields()) viewModel.login(email, password, onLoginSuccess) },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            enabled = !uiState.isLoading,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = AzulBoton)
                        ) {
                            if (uiState.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            else Text(text = "INGRESAR", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                        }
                    }

                    TextButton(onClick = onRegistrateClick, modifier = Modifier.fillMaxWidth()) {
                        Text(text = buildAnnotatedString {
                            append("¿No tienes cuenta?  ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = GrisTexto)) { append("Registrate") }
                        }, textAlign = TextAlign.Center, color = GrisTexto, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CampoUnderline(
    label:         String,
    placeholder:   String,
    value:         String,
    onValueChange: (String) -> Unit,
    keyboardType:  KeyboardType = KeyboardType.Text,
    esPassword:    Boolean = false,
    maxLength:     Int = Int.MAX_VALUE,
    error:         String? = null
) {
    Column {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF333333))
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            placeholder = { Text(placeholder, color = Color(0xFFBCBCBC), fontSize = 14.sp) },
            visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            isError = error != null,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = AzulBoton,
                unfocusedIndicatorColor = Color(0xFFE0E0E0),
                errorIndicatorColor = Color.Red,
                cursorColor = AzulBoton,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Text(text = error, color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}
