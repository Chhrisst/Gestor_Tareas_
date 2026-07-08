import crypto from "crypto";
import * as repo from "../repositories/trabajo.repository.mjs";

export const crear = async (data) => {
  const nuevoTrabajo = {
    id: crypto.randomUUID(),
    nombreCliente: data.nombreCliente,
    servicio: data.servicio,
    direccion: data.direccion,
    fecha: data.fecha,
    horario: data.horario,
    tecnicoId: data.tecnicoId,
    tecnicoNombre: data.tecnicoNombre
  };

  await repo.insertarTrabajo(nuevoTrabajo);
  return nuevoTrabajo;
};

export const listar = async () => {
  const result = await repo.listarTrabajos();
  return { items: result.Items || [] };
};
