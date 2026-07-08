import { PutCommand, GetCommand, UpdateCommand } from "@aws-sdk/lib-dynamodb";
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
