package com.org.gestor_tareas.data.repository

import com.org.gestor_tareas.domain.model.Tarea
import com.org.gestor_tareas.domain.repository.TareaRepository
import kotlinx.coroutines.delay


class TareaRepositoryImpl : TareaRepository {

    private val tareasMock = mutableMapOf(
        "TK-2026-001" to Tarea(
            id = "TK-2026-001",
            cliente = "Inversiones del Norte S.A.",
            direccion = "Av. Javier Prado Este 1520, San Isidro",
            estado = "Pendiente",
            distanciaKm = 2.5,
            latitudDestino = -12.0931,
            longitudDestino = -77.0465
        )
    )

    override suspend fun getTareaById(id: String): Tarea {
        delay(300) // simula latencia de red, bórralo cuando conectes el backend real
        return tareasMock[id] ?: throw Exception("Tarea no encontrada")
    }

    override suspend fun iniciarTrabajo(id: String): Tarea {
        delay(300)
        val tarea = tareasMock[id] ?: throw Exception("Tarea no encontrada")
        val actualizada = tarea.copy(estado = "En Progreso")
        tareasMock[id] = actualizada
        return actualizada
    }
}
