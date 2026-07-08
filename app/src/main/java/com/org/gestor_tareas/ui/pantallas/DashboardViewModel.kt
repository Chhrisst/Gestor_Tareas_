package com.org.gestor_tareas.ui.pantallas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.gestor_tareas.domain.usecase.TecnicoUseCases
import com.org.gestor_tareas.domain.usecase.ObtenerEstadoOrdenesUseCase
import com.org.gestor_tareas.domain.model.EstadoOrdenes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Intervalo de actualización automática en milisegundos (30 segundos)
private const val REFRESH_INTERVAL_MS = 30_000L

data class DashboardUiState(
    val totalTecnicos: Int = 0,
    val tecnicosOcupados: Int = 0,
    val tecnicosEspera: Int = 0,
    val tecnicosInactivos: Int = 0,
    val estadoOrdenes: EstadoOrdenes? = null,
    val isLoading: Boolean = true
)

class DashboardViewModel(
    private val tecnicoUseCases: TecnicoUseCases,
    private val obtenerEstadoOrdenesUseCase: ObtenerEstadoOrdenesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarDatos()
        iniciarActualizacionEnTiempoReal()
    }

    /**
     * Carga completa inicial: técnicos + órdenes.
     */
    fun cargarDatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Obtener tecnicos
                val tecnicos = tecnicoUseCases.getTecnicos()
                val totalTecnicos = tecnicos.size

                // Los técnicos no tienen estado en este momento en el backend, por lo que pondremos a todos como "Activos" en espera.
                val tecnicosInactivos = 0
                val tecnicosOcupados = 0
                val tecnicosEspera = totalTecnicos

                // Obtener ordenes
                val estadoOrdenes = obtenerEstadoOrdenesUseCase()

                _uiState.update {
                    it.copy(
                        totalTecnicos = totalTecnicos,
                        tecnicosOcupados = tecnicosOcupados,
                        tecnicosEspera = tecnicosEspera,
                        tecnicosInactivos = tecnicosInactivos,
                        estadoOrdenes = estadoOrdenes,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Bucle de actualización en tiempo real: refresca solo el estado de órdenes
     * cada REFRESH_INTERVAL_MS milisegundos mientras el ViewModel esté activo.
     */
    private fun iniciarActualizacionEnTiempoReal() {
        viewModelScope.launch {
            while (true) {
                delay(REFRESH_INTERVAL_MS)
                try {
                    val estadoOrdenes = obtenerEstadoOrdenesUseCase()
                    _uiState.update { it.copy(estadoOrdenes = estadoOrdenes) }
                } catch (_: Exception) {
                    // Ignorar errores silenciosos en el refresco automático
                }
            }
        }
    }
}
