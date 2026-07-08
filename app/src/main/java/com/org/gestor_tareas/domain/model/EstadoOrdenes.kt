package com.org.gestor_tareas.domain.model

data class EstadoOrdenes(
    val totalTareas: Int,
    val finalizadas: Int,
    val enProceso: Int,
    val pendientes: Int
) {
    val porcentajeFinalizadas: Int
        get() = if (totalTareas == 0) 0 else (finalizadas * 100) / totalTareas

    val porcentajeEnProceso: Int
        get() = if (totalTareas == 0) 0 else (enProceso * 100) / totalTareas

    val porcentajePendientes: Int
        get() = if (totalTareas == 0) 0 else (pendientes * 100) / totalTareas
}
