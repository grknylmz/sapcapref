const cds = require("@sap/cds");

async function initDb(db) {
  if (cds.env.env !== "development") {
    return;
  }

  return cds.run([
    INSERT.into("db.TaskCollections")
      .columns("ID", "title", "color", "isDefault")
      .rows(
        ["00000000-0000-0000-0000-000000000002", "Life List", null, false],
        ["00000000-0000-0000-0000-000000000003", "Business", "FF00FF", false],
        ["00000000-0000-0000-0000-000000000004", "Other", null, false]
      ),
    INSERT.into("db.Tasks")
      .columns(
        "ID",
        "title",
        "collection_ID",
        "status_code",
        "priority_code",
        "dueDate",
        "dueTime",
        "isPlannedForMyDay"
      )
      .rows(
        [
          "00000000-0000-0000-0000-00000000000A",
          "Tax declaration",
          "00000000-0000-0000-0000-000000000001",
          "O",
          5,
          "2021-05-31",
          null,
          false,
        ],
        [
          "00000000-0000-0000-0000-00000000000B",
          "Groceries shopping",
          "00000000-0000-0000-0000-000000000001",
          "O",
          5,
          null,
          null,
          true,
        ],
        [
          "00000000-0000-0000-0000-00000000000C",
          "What is the meaning of life?",
          "00000000-0000-0000-0000-000000000002",
          "D",
          null,
          null,
          null,
          false,
        ],
        [
          "00000000-0000-0000-0000-00000000000D",
          "Very important presentation",
          "00000000-0000-0000-0000-000000000003",
          "O",
          1,
          "2021-04-27",
          "10:00:00",
          false,
        ],
        [
          "00000000-0000-0000-0000-00000000000E",
          "Call car shop",
          "00000000-0000-0000-0000-000000000001",
          "O",
          3,
          null,
          null,
          false,
        ],
        [
          "00000000-0000-0000-0000-00000000000F",
          "Buy book",
          "00000000-0000-0000-0000-000000000001",
          "O",
          5,
          null,
          null,
          false,
        ],
        [
          "00000000-0000-0000-0000-0000000000AA",
          "Buy printer",
          "00000000-0000-0000-0000-000000000001",
          "O",
          5,
          null,
          null,
          false,
        ],
        [
          "00000000-0000-0000-0000-0000000000AB",
          "Buy headphones",
          "00000000-0000-0000-0000-000000000001",
          "D",
          5,
          null,
          null,
          false,
        ]
      ),
  ]);
}

module.exports = initDb;
