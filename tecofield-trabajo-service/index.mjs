import * as trabajoController from "./controllers/trabajo.controller.mjs";

export const handler = async (event) => {
  try {
    const method = event.requestContext?.http?.method || event.httpMethod || "GET";

    if (method === "GET") return trabajoController.listarTrabajos(event);
    if (method === "POST") return trabajoController.crearTrabajo(event);

    return {
      statusCode: 405,
      body: JSON.stringify({ message: "Método no permitido o acción no reconocida" })
    };

  } catch (error) {
    console.error("Error inesperado:", error);
    return {
      statusCode: 500,
      body: JSON.stringify({ message: "Error interno del servidor" })
    };
  }
};
