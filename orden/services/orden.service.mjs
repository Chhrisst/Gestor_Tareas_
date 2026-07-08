import * as repo from "../repositories/orden.repository.mjs";

export const listarOrdenes = async () => {
  const result = await repo.getOrdenes();
  return result.Items || [];
};

export const obtenerOrden = async (id) => {
  const result = await repo.getOrdenById(id);
  if (!result.Item) throw new Error("Orden no encontrada");
  return result.Item;
};

export const crearOrden = async (data) => {
  // Generar un ID entero aleatorio que quepa en un Int de Kotlin (32-bit con signo positivo)
  const id = Math.floor(Math.random() * 1000000000) + 1;
  const codigo = `ORD-${String(id).slice(-6)}`;
  
  const nuevaOrden = {
    id,
    codigo,
    servicio: data.servicio,
    cliente_nombre: data.cliente_nombre,
    direccion: data.direccion,
    hora_atencion: data.hora_atencion,
    fecha: data.fecha,
    estado: data.estado || "PENDIENTE"
  };

  await repo.insertarOrden(nuevaOrden);
  return nuevaOrden;
};

export const modificarOrden = async (id, data) => {
  await obtenerOrden(id);

  const updates = {
    servicio: data.servicio,
    cliente_nombre: data.cliente_nombre,
    direccion: data.direccion,
    hora_atencion: data.hora_atencion,
    fecha: data.fecha,
    estado: data.estado
  };

  // Limpiar campos indefinidos
  Object.keys(updates).forEach(key => updates[key] === undefined && delete updates[key]);

  const result = await repo.actualizarOrden(id, updates);
  return result.Attributes;
};

export const borrarOrden = async (id) => {
  await obtenerOrden(id);
  await repo.eliminarOrden(id);
  return { mensaje: "Orden eliminada con éxito" };
};
