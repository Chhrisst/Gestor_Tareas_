const defaultHeaders = {
  "Content-Type": "application/json",
  "Access-Control-Allow-Origin": process.env.CORS_ORIGIN || "*",
  "Access-Control-Allow-Headers": "Content-Type,Authorization",
  "Access-Control-Allow-Methods": "GET,POST,PUT,DELETE,OPTIONS",
};

export const rawResponse = (statusCode, data = null) => ({
  statusCode,
  headers: defaultHeaders,
  body: data ? JSON.stringify(data) : null,
});

export const noContent = () => ({
  statusCode: 204,
  headers: defaultHeaders,
  body: null,
});

export const badRequest = (error) =>
  rawResponse(400, formatError(error));

export const notFound = (message = "Resource not found") =>
  rawResponse(404, { message });

export const internalError = (message = "Internal Server Error") =>
  rawResponse(500, { message });

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