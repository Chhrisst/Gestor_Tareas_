package com.org.gestor_tareas.data.mapper

import com.org.gestor_tareas.data.remote.dto.OrdenDto
import com.org.gestor_tareas.domain.model.OrdenTrabajo

fun OrdenDto.toDomain(): OrdenTrabajo {
    return OrdenTrabajo(
        id = this.id,
        codigo = this.codigo,
        servicio = this.servicio,
        clienteNombre = this.cliente_nombre,
        direccion = this.direccion,
        horaAtencion = this.hora_atencion,
        fecha = this.fecha,
        estado = this.estado
    )
}
