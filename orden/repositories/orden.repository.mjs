import { PutCommand, GetCommand, UpdateCommand, DeleteCommand, ScanCommand } from "@aws-sdk/lib-dynamodb";
import { ddb } from "../config/dynamo.mjs";

const TABLE = process.env.TABLE_NAME || "Ordenes";

export const getOrdenes = () =>
  ddb.send(new ScanCommand({
    TableName: TABLE,
  }));

export const getOrdenById = (id) =>
  ddb.send(new GetCommand({
    TableName: TABLE,
    Key: { id: Number(id) },
  }));

export const insertarOrden = (item) =>
  ddb.send(new PutCommand({
    TableName: TABLE,
    Item: {
      ...item,
      id: Number(item.id),
    },
  }));

export const actualizarOrden = (id, updates) => {
  const updateExpressionParts = [];
  const expressionAttributeNames = {};
  const expressionAttributeValues = {};

  Object.entries(updates).forEach(([key, value]) => {
    if (key === 'id') return;
    
    const attributeName = `#attr_${key}`;
    const attributeValue = `:val_${key}`;
    
    updateExpressionParts.push(`${attributeName} = ${attributeValue}`);
    expressionAttributeNames[attributeName] = key;
    expressionAttributeValues[attributeValue] = value;
  });

  if (updateExpressionParts.length === 0) return Promise.resolve();

  return ddb.send(new UpdateCommand({
    TableName: TABLE,
    Key: { id: Number(id) },
    UpdateExpression: `SET ${updateExpressionParts.join(", ")}`,
    ExpressionAttributeNames: expressionAttributeNames,
    ExpressionAttributeValues: expressionAttributeValues,
    ReturnValues: "ALL_NEW",
  }));
};

export const eliminarOrden = (id) =>
  ddb.send(new DeleteCommand({
    TableName: TABLE,
    Key: { id: Number(id) },
  }));
