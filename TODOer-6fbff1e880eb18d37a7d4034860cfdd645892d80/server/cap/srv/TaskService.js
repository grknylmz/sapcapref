const cds = require("@sap/cds");
const { ColorSpecification } = require("./specification/color");

/**
 * Implementation of the Task application service
 */
class TaskService extends cds.ApplicationService {
  async init() {
    this.before(
      ["CREATE", "UPDATE"],
      "Collections",
      this.validateCollectionColor
    );
    this.before("CREATE", "Collections", this.setCollectionDefaults);
    this.before("DELETE", "Collections", this.forbidDeletingDefaultCollection);
    this.on(["CREATE", "UPDATE"], "Collections", this.changeDefaultCollection);

    this.before(["UPDATE", "DELETE"], "Tasks", this.validateTaskStatus);
    this.before("UPDATE", "Tasks", this.validateTaskDueDate);
    this.before(["CREATE", "UPDATE"], "Tasks", this.validateTaskDueTime);
    this.before("CREATE", "Tasks", this.setTaskDefaults);

    this.on("setToDone", "Tasks", (req) => this.setTaskStatus("D", req));
    this.on("cancel", "Tasks", (req) => this.setTaskStatus("X", req));
    this.on("reopen", "Tasks", (req) => this.setTaskStatus("O", req));
    await super.init();
  }

  validateCollectionColor(req) {
    if (!req.data) return;

    const color = req.data.color;
    if (!color) return;

    if (!new ColorSpecification().isValid(color)) {
      req.reject(400, `Value '${color}' is not a valid color`);
      return;
    }
    req.data.color = color.toUpperCase();
  }

  setCollectionDefaults(req) {
    if (!req.data) return;
    const data = req.data;
    data.isDefault = data.isDefault || false;
  }

  async forbidDeletingDefaultCollection(req) {
    if (!req.data) return;
    const id = req.data.ID;
    const collection = await this.tx(req).run(
      SELECT.from("Collections", id, (c) => c.isDefault)
    );
    if (collection && collection.isDefault) {
      req.reject(
        400,
        `Collection '${id}' is the default collection and must not be deleted`
      );
    }
  }

  async changeDefaultCollection(req, next) {
    const collection = await next(req);

    if (req.data && req.data.isDefault && collection.isDefault) {
      await this.tx(req).run(
        UPDATE`Collections`.set`isDefault = false`.where`ID != ${collection.ID}`
      );
    }

    return collection;
  }

  async validateTaskStatus(req) {
    if (!req.data) return;
    const data = req.data;
    const { status_code: status } = await this._getTask(
      data.ID,
      ["status_code"],
      req
    );
    if (status !== "O") {
      req.reject(400, "Only open tasks can be updated or deleted");
    }
  }

  async _getTask(id, columns, req) {
    return this.tx(req).run(SELECT.one.from("Tasks", id).columns(...columns));
  }

  async validateTaskDueDate(req) {
    if (!req.data) return;
    const data = req.data;
    if (data.dueDate !== null) return;
    const dueTime = await this._getTaskDueTime(data, req);
    if (dueTime) {
      req.reject(400, "Due date must not be null, if due time is not null");
    }
  }

  async _getTaskDueTime(data, req) {
    if (data.dueTime) return data.dueTime;
    if (!data.ID) return;
    const result = await this._getTask(data.ID, ["dueTime"], req);
    return result ? result.dueTime : undefined;
  }

  async validateTaskDueTime(req) {
    if (!req.data) return;
    const data = req.data;
    if (!data.dueTime) return;
    const dueDate = await this._getTaskDueDate(data, req);
    if (!dueDate) {
      req.reject(400, "Due date must not be null, if due time is not null");
    }
  }

  async _getTaskDueDate(data, req) {
    if (data.dueDate) return data.dueDate;
    if (!data.ID) return;
    const result = await this._getTask(data.ID, ["dueDate"], req);
    return result ? result.dueDate : undefined;
  }

  setTaskDefaults(req) {
    if (!req.data) return;
    const data = req.data;
    data.status_code = "O";
    data.isPlannedForMyDay = data.isPlannedForMyDay || false;
  }

  async setTaskStatus(status, req) {
    const tasks = await this.tx(req).run(req.query);
    if (!tasks.length) {
      req.reject(404);
      return;
    }
    return cds.db.update("db.Tasks", tasks[0].ID).set({ status_code: status });
  }
}

module.exports = TaskService;
