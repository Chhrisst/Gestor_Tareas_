import { z } from "zod";

export const crearTrabajoSchema = z.object({
  nombreCliente: z.string().min(1, "El nombre del cliente es requerido"),
  servicio: z.string().min(1, "El servicio es requerido"),
  direccion: z.string().min(1, "La dirección es requerida"),
  fecha: z.string().min(1, "La fecha es requerida"),
  horario: z.string().min(1, "El horario es requerido"),
  tecnicoId: z.number(),
  tecnicoNombre: z.string().min(1, "El nombre del técnico es requerido")
});
