import * as ordenController from "./controllers/orden.controller.mjs";

export const handler = async (event) => {
  try {
    const path = event.rawPath || event.path || "";
    const method = event.requestContext?.http?.method || event.httpMethod || "GET";
    const cleanPath = path.replace(/\/$/, ""); // eliminar barra diagonal al final

    console.log(`Petición recibida: ${method} ${path}`);

    // Soporte para peticiones preflight CORS (OPTIONS)
    if (method === "OPTIONS") {
      return {
        statusCode: 200,
        headers: {
          "Access-Control-Allow-Origin": process.env.CORS_ORIGIN || "*",
          "Access-Control-Allow-Headers": "Content-Type,Authorization",
          "Access-Control-Allow-Methods": "GET,POST,PUT,DELETE,OPTIONS",
        },
        body: null
      };
    }

    // Enrutamiento para colección /ordenes
    if (cleanPath.endsWith("/ordenes")) {
      if (method === "GET") {
        return await ordenController.getOrdenes(event);
      }
      if (method === "POST") {
        return await ordenController.addOrden(event);
      }
    }

    // Enrutamiento para elemento individual /ordenes/{id}
    const match = cleanPath.match(/\/ordenes\/([^\/]+)$/);
    if (match) {
      if (method === "GET") {
        return await ordenController.getOrdenById(event);
      }
      if (method === "PUT") {
        return await ordenController.updateOrden(event);
      }
      if (method === "DELETE") {
        return await ordenController.deleteOrden(event);
      }
    }

    return {
      statusCode: 404,
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": process.env.CORS_ORIGIN || "*",
      },
      body: JSON.stringify({ message: `Ruta no encontrada: ${method} ${path}` })
    };

  } catch (error) {
    console.error("Error inesperado en handler:", error);
    return {
      statusCode: 500,
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": process.env.CORS_ORIGIN || "*",
      },
      body: JSON.stringify({ message: "Error interno del servidor", error: error.message })
    }; 
  }
};
