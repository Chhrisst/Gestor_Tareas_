import * as authController from "./controllers/auth.controller.mjs";

export const handler = async (event) => {
  try {
    const method = event.requestContext?.http?.method || event.httpMethod || "POST";
    const body = typeof event.body === 'string' ? JSON.parse(event.body) : event.body;

    if (method === "POST") {
        if (body.accion === "registro") return authController.registrarUsuario(event);
        if (body.accion === "login") return authController.loginUsuario(event);
        if (body.accion === "google-login") return authController.loginGoogle(event);
        if (body.accion === "cambiar-rol") return authController.cambiarRol(event);
        if (body.accion === "verificar-sesion") return authController.verificarSesion(event);
    }

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
