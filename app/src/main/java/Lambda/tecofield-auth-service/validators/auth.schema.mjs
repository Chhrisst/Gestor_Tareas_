import { z } from "zod";

const nameRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;

export const registroSchema = z.object({
  email: z.string().email("Formato de correo inválido"),
  password: z.string().min(8, "La contraseña debe tener al menos 8 caracteres"),
  nombre: z.string()
    .min(3, "El nombre debe tener al menos 3 letras")
    .regex(nameRegex, "El nombre solo debe contener letras")
});

export const loginSchema = z.object({
  email: z.string().email("Formato de correo inválido"),
  password: z.string().min(1, "La contraseña es requerida")
});

export const googleLoginSchema = z.object({
  idToken: z.string().min(1, "El ID Token es requerido")
});

export const cambiarRolSchema = z.object({
  email: z.string().email(),
  nuevoRol: z.enum(["ADMIN", "TECNICO", "PENDIENTE", "INACTIVO"])
});

export const verificarSesionSchema = z.object({
  email: z.string().email()
});
