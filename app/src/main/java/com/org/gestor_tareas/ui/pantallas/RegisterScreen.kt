package com.org.gestor_tareas.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.org.gestor_tareas.ui.pantallas.auth.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: (String, String) -> Unit,
    onIniciarSesionClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    // Errores individuales
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }
    var confirmPassError by remember { mutableStateOf<String?>(null) }

    val backgroundGradient = Brush.verticalGradient(colors = listOf(Color(0xFF0D1B2A), Color(0xFF000814)))
    val buttonGradient = Brush.horizontalGradient(colors = listOf(Color(0xFF0A369B), Color(0xFF000814)))

    fun validateFields(): Boolean {
        nameError = if (fullName.length < 3) "Mínimo 3 caracteres" else null
        emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Formato de correo inválido" else null
        passError = if (password.length < 8) "Mínimo 8 caracteres" else null
        confirmPassError = if (password != confirmPassword) "Las contraseñas no coinciden" else null
        
        return nameError == null && emailError == null && passError == null && confirmPassError == null
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundGradient)) {
        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(text = "Crea tu cuenta", color = Color.White, fontSize = 32.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Light)
            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 30.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    
                    CampoUnderline(
                        label = "Nombre completo",
                        placeholder = "John Smith",
                        value = fullName,
                        onValueChange = { if (it.all { c -> c.isLetter() || c.isWhitespace() }) { fullName = it; nameError = null } },
                        maxLength = 256,
                        error = nameError
                    )

                    CampoUnderline(
                        label = "Correo Electrónico",
                        placeholder = "john@email.com",
                        value = email,
                        onValueChange = { email = it; emailError = null },
                        keyboardType = KeyboardType.Email,
                        maxLength = 128,
                        error = emailError
                    )

                    CampoUnderline(
                        label = "Contraseña",
                        placeholder = "••••••••",
                        value = password,
                        onValueChange = { password = it; passError = null },
                        esPassword = true,
                        maxLength = 128,
                        error = passError
                    )

                    CampoUnderline(
                        label = "Confirmar contraseña",
                        placeholder = "••••••••",
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; confirmPassError = null },
                        esPassword = true,
                        maxLength = 128,
                        error = confirmPassError
                    )

                    if (uiState.error != null) {
                        Text(text = uiState.error!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(top = 8.dp), textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { if (validateFields()) viewModel.register(fullName, email, password, onRegisterSuccess) },
                        enabled = !uiState.isLoading,
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize().background(buttonGradient), contentAlignment = Alignment.Center) {
                            if (uiState.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            else Text(text = "REGISTRARSE", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(text = "Iniciar Sesión", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(top = 10.dp).clickable { onIniciarSesionClick() }, textAlign = TextAlign.Center)
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
