import express from "express";
import YAML from "yamljs";
import path from "path";
import { connector } from "swagger-routes-express";
import * as apiHandlers from "./api";
import { handleError } from "./middleware/error";

const app = express();

app.set("port", process.env.PORT || 3000);

// Setup body parsers
app.use(express.json());
app.use(express.text());
app.use(express.urlencoded({ extended: false }));

// Connect API and app
const apiDefinition = YAML.load(path.join("resources", "api.yaml"));
const connect = connector(apiHandlers, apiDefinition);
connect(app);

// Add error handler
app.use(handleError);

export default app;
