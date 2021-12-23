const test = require("@sap/cds/lib/utils/tests").in(__dirname, "..");
const cds = require("@sap/cds/lib");

if (cds.User.default) {
  cds.User.default = cds.User.Privileged;
} else {
  cds.User = cds.User.Privileged;
}

module.exports = test(".");
