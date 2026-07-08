package com.org.gestor_tareas.ui.pantallas.tecnicos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.gestor_tareas.domain.model.OrdenTrabajo

private val FondoGradientInicio = Color(0xFF001A4D)
private val FondoGradientFin = Color(0xFF000000)
private val AzulAcento = Color(0xFF0033CC)
private val GrisFondoChips = Color(0xFF262626)
private val TextoSecundario = Color(0xFFB3B3B3)

@Composable
fun InicioTecnicoScreen(
    viewModel: OrdenTecnicoViewModel,
    onCerrarSesionClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(FondoGradientInicio, FondoGradientFin)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    // Aquí iría la imagen de perfil real
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp).align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Hola, ${uiState.nombreTecnico}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = uiState.fechaActual,
                        color = TextoSecundario,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Barra de Búsqueda
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                placeholder = { Text("Buscar ID, Cliente o Dirección...", color = Color.Gray, fontSize = 14.sp) },
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val filtros = listOf(
                    Triple("Todas", Color.Gray, "Todas"),
                    Triple("Pendientes", Color.Yellow, "Pendiente"),
                    Triple("En Proceso", Color.Blue, "En Proceso"),
                    Triple("Finalizadas", Color.Green, "Finalizada")
                )
                items(filtros) { (label, color, valor) ->
                    Surface(
                        onClick = { viewModel.onFiltroChange(valor) },
                        shape = CircleShape,
                        color = if (uiState.filtroEstado == valor) AzulAcento.copy(alpha = 0.3f) else GrisFondoChips,
                        modifier = Modifier.height(32.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = label, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de Órdenes (con weight para no empujar el botón de logout fuera)
            val listaFiltrada = uiState.ordenes.filter {
                (uiState.filtroEstado == "Todas" || it.estado == uiState.filtroEstado) &&
                (it.codigo.contains(uiState.searchQuery, true) || 
                 it.clienteNombre.contains(uiState.searchQuery, true) ||
                 it.direccion.contains(uiState.searchQuery, true))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(listaFiltrada) { orden ->
                    OrdenTrabajoCard(orden = orden)
                }
            }
            
            // BOTÓN CERRAR SESIÓN
            Button(
                onClick = onCerrarSesionClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(
                    text = "Cerrar Sesión",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
        }
    }
}

@Composable
fun OrdenTrabajoCard(orden: OrdenTrabajo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = orden.codigo,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = orden.servicio,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Text(text = "Cliente", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = orden.clienteNombre,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = orden.direccion, fontSize = 11.sp, color = Color.Black)
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = orden.horaAtencion, fontSize = 11.sp, color = Color.Gray)
                    }
                }
                
                // Icono de Mapa grande como en la imagen
                Icon(
                    Icons.Default.Map, 
                    contentDescription = "Mapa", 
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { /* Abrir mapa */ },
                    tint = Color.Black
                )
            }
            
            // Footer con el estado y degradado
            val colorFooter = when(orden.estado) {
                "En Proceso" -> Brush.horizontalGradient(listOf(Color(0xFF0033CC), Color(0xFF001133)))
                "Finalizada" -> Brush.horizontalGradient(listOf(Color(0xFF009933), Color(0xFF004411)))
                else -> Brush.horizontalGradient(listOf(Color(0xFF808080), Color(0xFF404040)))
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(colorFooter),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = orden.estado, 
                    color = Color.White, 
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp
                )
            }
        }
    }
}
