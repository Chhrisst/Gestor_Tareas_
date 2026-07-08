import { z } from "zod";

export const createOrdenSchema = z.object({
  servicio: z.string().min(1, "El servicio es requerido"),
  cliente_nombre: z.string().min(1, "El nombre del cliente es requerido"),
  direccion: z.string().min(1, "La dirección es requerida"),
  hora_atencion: z.string().min(1, "La hora de atención es requerida"),
  fecha: z.string().min(1, "La fecha es requerida"),
  estado: z.string().min(1, "El estado es requerido")
});

export const updateOrdenSchema = z.object({
  servicio: z.string().min(1, "El servicio es requerido"),
  cliente_nombre: z.string().min(1, "El nombre del cliente es requerido"),
  direccion: z.string().min(1, "La dirección es requerida"),
  hora_atencion: z.string().min(1, "La hora de atención es requerida"),
  fecha: z.string().min(1, "La fecha es requerida"),
  estado: z.string().min(1, "El estado es requerido")
});
