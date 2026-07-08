const defaultHeaders = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": process.env.CORS_ORIGIN || "*",
    "Access-Control-Allow-Headers": "Content-Type,Authorization",
    "Access-Control-Allow-Methods": "GET,POST,PUT,DELETE,OPTIONS",
  };

  const buildResponse = (statusCode, data = null) => ({
    statusCode,
    headers: defaultHeaders,
    body: data ? JSON.stringify({
      success: statusCode < 400,
      data: statusCode < 400 ? data : undefined,
      error: statusCode >= 400 ? data : undefined,
      timestamp: new Date().toISOString(),
    }) : null,
  });

  export const ok = (data) => buildResponse(200, data);

  export const created = (data) => buildResponse(201, data);

  export const noContent = () => ({
    statusCode: 204,
    headers: defaultHeaders,
    body: null,
  });

  export const badRequest = (error) =>
    buildResponse(400, formatError(error));

  export const notFound = (message = "Resource not found") =>
    buildResponse(404, { message });

  export const internalError = (message = "Internal Server Error") =>
    buildResponse(500, { message });

  // Respuesta SIN envolver en {success,data,...}: la usan los endpoints
  // que en Android esperan el objeto directo (Response<TecnicoDto>)
  export const rawOk = (data) => ({
    statusCode: 200,
    headers: defaultHeaders,
    body: JSON.stringify(data),
  });

  export const rawCreated = (data) => ({
    statusCode: 201,
    headers: defaultHeaders,
    body: JSON.stringify(data),
  });

  // Normalizador de errores (ej: Zod)
  const formatError = (error) => {
    if (error?.issues) {
      return {
        message: "Validation failed",
        details: error.issues.map(e => ({
          field: e.path.join("."),
          message: e.message
        }))
      };
    }

    return {
      message: error?.message || error
    };
  };
