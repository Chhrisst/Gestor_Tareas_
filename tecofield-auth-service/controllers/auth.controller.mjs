import * as service from "../services/auth.service.mjs";
import { registroSchema, loginSchema, googleLoginSchema, cambiarRolSchema, verificarSesionSchema } from "../validators/auth.schema.mjs";
import { ok, created, badRequest } from "../utils/response.mjs";

export const registrarUsuario = async (event) => {
  try {
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    const parsed = registroSchema.safeParse(body);
    if (!parsed.success) return badRequest(parsed.error);
    const res = await service.registro(parsed.data);
    return created(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const loginUsuario = async (event) => {
  try {
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    const parsed = loginSchema.safeParse(body);
    if (!parsed.success) return badRequest(parsed.error);
    const res = await service.login(parsed.data);
    return ok(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const loginGoogle = async (event) => {
  try {
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    const parsed = googleLoginSchema.safeParse(body);
    if (!parsed.success) return badRequest(parsed.error);
    const res = await service.loginGoogle(parsed.data.idToken);
    return ok(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const cambiarRol = async (event) => {
  try {
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    const parsed = cambiarRolSchema.safeParse(body);
    if (!parsed.success) return badRequest(parsed.error);
    const res = await service.cambiarRol(parsed.data.email, parsed.data.nuevoRol);
    return ok(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const verificarSesion = async (event) => {
  try {
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    const parsed = verificarSesionSchema.safeParse(body);
    if (!parsed.success) return badRequest(parsed.error);
    const res = await service.verificarSesion(parsed.data.email);
    return ok(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const listarUsuarios = async (event) => {
  try {
    const res = await service.listarUsuarios();
    return ok(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const eliminarUsuario = async (event) => {
  try {
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    if (!body.email) return badRequest({ message: 'Email requerido' });
    const res = await service.eliminarUsuario(body.email);
    return ok(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

export const actualizarUsuario = async (event) => {
  try {
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;
    if (!body.email) return badRequest({ message: 'Email requerido' });
    const res = await service.actualizarUsuario(body);
    return ok(res);
  } catch (error) {
    return badRequest({ message: error.message });
  }
};

