import { PutCommand, ScanCommand } from "@aws-sdk/lib-dynamodb";
import { ddb } from "../config/dynamo.mjs";

const TABLE = process.env.TABLE_NAME || "Trabajos";

export const insertarTrabajo = (item) =>
  ddb.send(new PutCommand({
    TableName: TABLE,
    Item: item,
  }));

export const listarTrabajos = () =>
  ddb.send(new ScanCommand({
    TableName: TABLE,
  }));
