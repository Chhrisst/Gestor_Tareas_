import crypto from "crypto";
import * as repo from "../repositories/auth.repository.mjs";

const SECRET = "mi_secreto_super_seguro_para_gestor_tareas";

const ROLES = {
  ADMIN: "ADMIN",
  TECNICO: "TECNICO",
  PENDIENTE: "PENDIENTE",
  INACTIVO: "INACTIVO"
};

export const registro = async (data) => {
  const existCheck = await repo.getUsuarioByEmail(data.email);
  if (existCheck.Item) throw new Error("El usuario ya está registrado");

  const salt = crypto.randomBytes(16).toString('hex');
  const hash = crypto.pbkdf2Sync(data.password, salt, 1000, 64, 'sha512').toString('hex');

  const nuevoUsuario = {
    email: data.email,
    nombre: data.nombre,
    hash,
    salt,
    rol: ROLES.PENDIENTE
  };

  await repo.insertarUsuario(nuevoUsuario);
  const token = generarToken(nuevoUsuario);
  return { token, nombre: nuevoUsuario.nombre, email: nuevoUsuario.email, rol: nuevoUsuario.rol };
};

export const login = async (data) => {
  const result = await repo.getUsuarioByEmail(data.email);
  const user = result.Item;

  if (!user) throw new Error("Usuario no existe o contraseña incorrecta");
  if (!user.hash) throw new Error("Esta cuenta usa inicio con Google. Usa el botón de Google.");
  if (user.rol === ROLES.INACTIVO) throw new Error("Tu cuenta está deshabilitada");

  const hashCalculado = crypto.pbkdf2Sync(data.password, user.salt, 1000, 64, 'sha512').toString('hex');
  if (hashCalculado !== user.hash) throw new Error("Usuario no existe o contraseña incorrecta");

  const token = generarToken(user);
  return { token, nombre: user.nombre, email: user.email, rol: user.rol };
};

export const loginGoogle = async (idToken) => {
  const response = await fetch(`https://oauth2.googleapis.com/tokeninfo?id_token=${idToken}`);
  const payload = await response.json();
  if (!payload.email) throw new Error("Token de Google inválido");

  let result = await repo.getUsuarioByEmail(payload.email);
  let user = result.Item;

  if (!user) {
    user = {
      email: payload.email,
      nombre: payload.name || "Usuario Google",
      rol: ROLES.PENDIENTE,
      metodo: "google"
    };
    await repo.insertarUsuario(user);
  }

  if (user.rol === ROLES.INACTIVO) throw new Error("Tu cuenta está deshabilitada");

  const token = generarToken(user);
  return { token, nombre: user.nombre, email: user.email, rol: user.rol };
};

export const cambiarRol = async (email, nuevoRol) => {
  if (!Object.values(ROLES).includes(nuevoRol)) throw new Error("Rol no válido");
  await repo.actualizarRolUsuario(email, nuevoRol);
  return { mensaje: `Rol actualizado a ${nuevoRol} para ${email}` };
};

export const verificarSesion = async (email) => {
  const result = await repo.getUsuarioByEmail(email);
  const user = result.Item;

  if (!user) throw new Error("Usuario no encontrado");

  // Devolvemos el estado actual del usuario en la base de datos
  return {
    email: user.email,
    rol: user.rol,
    nombre: user.nombre,
    activo: user.rol !== ROLES.INACTIVO
  };
};

function generarToken(user) {
  const header = Buffer.from(JSON.stringify({ alg: "HS256", typ: "JWT" })).toString('base64url');
  const payload = Buffer.from(JSON.stringify({
    email: user.email,
    nombre: user.nombre,
    rol: user.rol
  })).toString('base64url');
  const signature = crypto.createHmac('sha256', SECRET).update(`${header}.${payload}`).digest('base64url');
  return `${header}.${payload}.${signature}`;
}

export const listarUsuarios = async () => {
  const result = await repo.listarUsuarios();
  return { items: (result.Items || []).map(u => ({ email: u.email, nombre: u.nombre, rol: u.rol, metodo: u.metodo || 'email' })) };
};

export const eliminarUsuario = async (email) => {
  await repo.eliminarUsuario(email);
  return { mensaje: `Usuario ${email} eliminado` };
};

export const actualizarUsuario = async (data) => {
  const result = await repo.getUsuarioByEmail(data.email);
  const user = result.Item;
  if (!user) throw new Error("El usuario no existe");

  const updates = {
    nombre: data.nombre,
    rol: data.rol,
  };

  if (data.password && data.password.trim().length > 0) {
    const salt = crypto.randomBytes(16).toString('hex');
    const hash = crypto.pbkdf2Sync(data.password, salt, 1000, 64, 'sha512').toString('hex');
    updates.hash = hash;
    updates.salt = salt;
  }

  await repo.actualizarUsuario(data.email, updates);
  return { email: data.email, nombre: data.nombre, rol: data.rol };
};
