package com.org.gestor_tareas.data.mapper

import com.org.gestor_tareas.data.remote.dto.TecnicoDto
import com.org.gestor_tareas.domain.model.Tecnico

fun TecnicoDto.toDomain(): Tecnico {
    return Tecnico(
        id = id,
        nombre = nombre,
        rol = rol
    )
}