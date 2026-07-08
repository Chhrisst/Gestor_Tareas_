import { PutCommand, GetCommand, UpdateCommand, ScanCommand, DeleteCommand } from "@aws-sdk/lib-dynamodb";
import { ddb } from "../config/dynamo.mjs";

const TABLE = process.env.TABLE_NAME || "Usuarios";

export const insertarUsuario = (item) =>
  ddb.send(new PutCommand({
    TableName: TABLE,
    Item: item,
  }));

export const getUsuarioByEmail = (email) =>
  ddb.send(new GetCommand({
    TableName: TABLE,
    Key: { email },
  }));

export const actualizarRolUsuario = (email, nuevoRol) =>
  ddb.send(new UpdateCommand({
    TableName: TABLE,
    Key: { email },
    UpdateExpression: "SET rol = :r",
    ExpressionAttributeValues: {
      ":r": nuevoRol,
    },
  }));

export const listarUsuarios = () =>
  ddb.send(new ScanCommand({
    TableName: TABLE,
    ProjectionExpression: "email, nombre, rol, metodo",
  }));

export const eliminarUsuario = (email) =>
  ddb.send(new DeleteCommand({
    TableName: TABLE,
    Key: { email },
  }));

export const actualizarUsuario = (email, updates) => {
  let UpdateExpression = "SET nombre = :n, rol = :r";
  const ExpressionAttributeValues = {
    ":n": updates.nombre,
    ":r": updates.rol,
  };
  const ExpressionAttributeNames = {};

  if (updates.hash && updates.salt) {
    UpdateExpression += ", #h = :hashVal, salt = :saltVal";
    ExpressionAttributeValues[":hashVal"] = updates.hash;
    ExpressionAttributeValues[":saltVal"] = updates.salt;
    ExpressionAttributeNames["#h"] = "hash";
  }

  return ddb.send(new UpdateCommand({
    TableName: TABLE,
    Key: { email },
    UpdateExpression,
    ExpressionAttributeNames: Object.keys(ExpressionAttributeNames).length > 0 ? ExpressionAttributeNames : undefined,
    ExpressionAttributeValues,
  }));
};

