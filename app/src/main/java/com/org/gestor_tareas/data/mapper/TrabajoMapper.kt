package com.org.gestor_tareas.data.mapper

import com.org.gestor_tareas.data.remote.dto.TrabajoDto
import com.org.gestor_tareas.domain.model.Trabajo

fun TrabajoDto.toDomain(): Trabajo {
    return Trabajo(
        id = id,
        nombreCliente = nombreCliente,
        servicio = servicio,
        direccion = direccion,
        fecha = fecha,
        horario = horario,
        tecnicoId = tecnicoId,
        tecnicoNombre = tecnicoNombre
    )
}
