var faas = require("./determine-stockledger-group.js");

faas.handler({"test":new Error("TEST")}, null);