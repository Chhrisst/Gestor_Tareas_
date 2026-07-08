import * as service from "../services/trabajo.service.mjs";
import { crearTrabajoSchema } from "../validators/trabajo.schema.mjs";
import { ok, rawCreated, badRequest } from "../utils/response.mjs";

export const listarTrabajos = async (event) => {
  try {
    const res = await service.listar();
    return ok(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const crearTrabajo = async (event) => {
  try {
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    const parsed = crearTrabajoSchema.safeParse(body);
    if (!parsed.success) return badRequest(parsed.error);
    const res = await service.crear(parsed.data);
    return rawCreated(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};
