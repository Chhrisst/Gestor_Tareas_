import * as service from "../services/orden.service.mjs";
import { rawResponse, noContent, badRequest, notFound } from "../utils/response.mjs";
import { createOrdenSchema, updateOrdenSchema } from "../validators/orden.schema.mjs";

export const getOrdenes = async (event) => {
  try {
    const items = await service.listarOrdenes();
    // GET /ordenes espera Response<ApiResponse<OrdenDataDto>>
    // ApiResponse tiene la estructura { data: T }, y OrdenDataDto { items: List<OrdenDto> }
    // Por ende, la respuesta final debe ser: { data: { items: [...] } }
    return rawResponse(200, { data: { items } });
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const getOrdenById = async (event) => {
  try {
    const id = event.pathParameters?.id || event.path?.split("/").pop();
    if (!id) return badRequest({ message: "ID es requerido" });
    const item = await service.obtenerOrden(id);
    return rawResponse(200, item);
  } catch (error) {
    if (error.message === "Orden no encontrada") {
      return notFound(error.message);
    }
    return badRequest({ message: error.message });
  }
};

export const addOrden = async (event) => {
  try {
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    const parsed = createOrdenSchema.safeParse(body);
    if (!parsed.success) return badRequest(parsed.error);

    const res = await service.crearOrden(parsed.data);
    // POST /ordenes espera Response<OrdenDto> directamente
    return rawResponse(201, res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const updateOrden = async (event) => {
  try {
    let id = event.pathParameters?.id;
    if (!id) {
      const match = (event.rawPath || event.path || "").match(/\/ordenes\/([^\/]+)/);
      if (match) id = match[1];
    }
    if (!id) return badRequest({ message: "ID es requerido" });

    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    const parsed = updateOrdenSchema.safeParse(body);
    if (!parsed.success) return badRequest(parsed.error);

    const res = await service.modificarOrden(id, parsed.data);
    // PUT /ordenes/{id} espera Response<OrdenDto> directamente
    return rawResponse(200, res);
  } catch (error) {
    if (error.message === "Orden no encontrada") {
      return notFound(error.message);
    }
    return badRequest({ message: error.message });
  }
};

export const deleteOrden = async (event) => {
  try {
    let id = event.pathParameters?.id;
    if (!id) {
      const match = (event.rawPath || event.path || "").match(/\/ordenes\/([^\/]+)/);
      if (match) id = match[1];
    }
    if (!id) return badRequest({ message: "ID es requerido" });

    await service.borrarOrden(id);
    return noContent();
  } catch (error) {
    if (error.message === "Orden no encontrada") {
      return notFound(error.message);
    }
    return badRequest({ message: error.message });
  }
};
