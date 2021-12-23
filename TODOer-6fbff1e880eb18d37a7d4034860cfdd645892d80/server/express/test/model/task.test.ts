import Task from "../../src/model/task";
import TaskPriority from "../../src/model/taskPriority";
import TaskStatus from "../../src/model/taskStatus";

describe("Creating a Task entity", () => {
  it("succeeds with minimum properties", () => {
    const task = new Task(42, {
      collection: { id: 1 },
      status: new TaskStatus("O", "open"),
    });
    expect(task.id).toEqual(42);
    expect(task.title).toBeUndefined();
    expect(task.collection.id).toEqual(1);
    expect(task.dueDate).toBeUndefined();
    expect(task.dueTime).toBeUndefined();
    expect(task.status.code).toEqual("O");
    expect(task.status.name).toEqual("open");
    expect(task.isUpdatable).toBeTruthy();
    expect(task.isDeletable).toBeTruthy();
    expect(task.priority).toBeUndefined();
    expect(task.isPlannedForMyDay).toBeFalsy();
  });

  it("succeeds with all properties", () => {
    const task = new Task(42, {
      title: "TODO",
      collection: { id: 1, title: "My Tasks" },
      dueDate: "2021-04-07",
      dueTime: "17:40:35",
      status: new TaskStatus("O", "open"),
      priority: new TaskPriority(3, "medium"),
      isPlannedForMyDay: true,
    });
    expect(task.id).toEqual(42);
    expect(task.title).toEqual("TODO");
    expect(task.collection.id).toEqual(1);
    expect(task.collection.title).toEqual("My Tasks");
    expect(task.dueDate).toEqual("2021-04-07");
    expect(task.dueTime).toEqual("17:40:35");
    expect(task.status.code).toEqual("O");
    expect(task.status.name).toEqual("open");
    expect(task.isUpdatable).toBeTruthy();
    expect(task.isDeletable).toBeTruthy();
    expect(task.priority?.code).toEqual(3);
    expect(task.priority?.name).toEqual("medium");
    expect(task.isPlannedForMyDay).toBeTruthy();
  });

  it("fails with too long title", () => {
    expect(
      () =>
        new Task(42, {
          title:
            "Creating a new task with a title that is exceeding the 40 characters limit",
          collection: { id: 1 },
          status: new TaskStatus("O", "open"),
        })
    ).toThrow();
  });

  it("fails with malformed due date", () => {
    expect(
      () =>
        new Task(42, {
          collection: { id: 1 },
          dueDate: "2021-14-07",
          status: new TaskStatus("O", "open"),
        })
    ).toThrow();
  });

  it("fails with malformed due time", () => {
    expect(
      () =>
        new Task(42, {
          collection: { id: 1 },
          dueDate: "2021-04-07",
          dueTime: "17_45_00",
          status: new TaskStatus("O", "open"),
        })
    ).toThrow();
  });

  it("succeeds with due date but without time", () => {
    const task = new Task(42, {
      collection: { id: 1 },
      dueDate: "2021-04-07",
      status: new TaskStatus("O", "open"),
    });
    expect(task.id).toEqual(42);
    expect(task.title).toBeUndefined();
    expect(task.collection.id).toEqual(1);
    expect(task.dueDate).toEqual("2021-04-07");
    expect(task.dueTime).toBeUndefined();
    expect(task.status.code).toEqual("O");
    expect(task.status.name).toEqual("open");
    expect(task.isUpdatable).toBeTruthy();
    expect(task.isDeletable).toBeTruthy();
    expect(task.priority).toBeUndefined();
    expect(task.isPlannedForMyDay).toBeFalsy();
  });

  it("fails without due date but with time", () => {
    expect(
      () =>
        new Task(42, {
          collection: { id: 1 },
          dueTime: "17:45:00",
          status: new TaskStatus("O", "open"),
        })
    ).toThrow();
  });

  it("with status 'done'", () => {
    const task = new Task(42, {
      collection: { id: 1 },
      status: new TaskStatus("D", "done"),
    });
    expect(task.id).toEqual(42);
    expect(task.title).toBeUndefined();
    expect(task.collection.id).toEqual(1);
    expect(task.dueDate).toBeUndefined();
    expect(task.dueTime).toBeUndefined();
    expect(task.status.code).toEqual("D");
    expect(task.status.name).toEqual("done");
    expect(task.isUpdatable).toBeFalsy();
    expect(task.isDeletable).toBeFalsy();
    expect(task.priority).toBeUndefined();
    expect(task.isPlannedForMyDay).toBeFalsy();
  });

  it("with status 'cancelled'", () => {
    const task = new Task(42, {
      collection: { id: 1 },
      status: new TaskStatus("X", "cancelled"),
    });
    expect(task.id).toEqual(42);
    expect(task.title).toBeUndefined();
    expect(task.collection.id).toEqual(1);
    expect(task.dueDate).toBeUndefined();
    expect(task.dueTime).toBeUndefined();
    expect(task.status.code).toEqual("X");
    expect(task.status.name).toEqual("cancelled");
    expect(task.isUpdatable).toBeFalsy();
    expect(task.isDeletable).toBeFalsy();
    expect(task.priority).toBeUndefined();
    expect(task.isPlannedForMyDay).toBeFalsy();
  });
});

describe("Updating a Task entity", () => {
  it("fails with due date but without time", () => {
    const task = new Task(42, {
      collection: { id: 1 },
      status: new TaskStatus("O", "open"),
    });
    expect(() => (task.dueTime = "17:45:00")).toThrow();
  });

  it("with null due date also nulls due time", () => {
    const task = new Task(42, {
      collection: { id: 1 },
      dueDate: "2021-04-07",
      dueTime: "17:40:35",
      status: new TaskStatus("O", "open"),
    });
    expect(task.dueTime).toEqual("17:40:35");
    task.dueDate = undefined;
    expect(task.dueDate).toBeUndefined();
    expect(task.dueTime).toBeUndefined();
  });

  it("is not working for done task", () => {
    const task = new Task(42, {
      title: "TODO",
      collection: { id: 1, title: "My Tasks" },
      dueDate: "2021-04-07",
      dueTime: "17:40:35",
      status: new TaskStatus("D", "done"),
      priority: new TaskPriority(3, "medium"),
      isPlannedForMyDay: true,
    });
    expect(task.id).toEqual(42);
    expect(task.title).toEqual("TODO");
    expect(task.collection.id).toEqual(1);
    expect(task.collection.title).toEqual("My Tasks");
    expect(task.dueDate).toEqual("2021-04-07");
    expect(task.dueTime).toEqual("17:40:35");
    expect(task.status.code).toEqual("D");
    expect(task.status.name).toEqual("done");
    expect(task.isUpdatable).toBeFalsy();
    expect(task.isDeletable).toBeFalsy();
    expect(task.priority?.code).toEqual(3);
    expect(task.priority?.name).toEqual("medium");
    expect(task.isPlannedForMyDay).toBeTruthy();

    task.title = "updated title";
    task.dueTime = "18:00:00";
    task.priority = new TaskPriority(1, "high");
    task.isPlannedForMyDay = false;
    expect(task.title).toEqual("TODO");
    expect(task.dueTime).toEqual("17:40:35");
    expect(task.priority?.code).toEqual(3);
    expect(task.priority?.name).toEqual("medium");
    expect(task.isPlannedForMyDay).toBeTruthy();
  });

  it("by setting status to 'done'", () => {
    const task = new Task(42, {
      collection: { id: 1 },
      status: new TaskStatus("O", "open"),
    });
    task.status = new TaskStatus("D", "done");
    expect(task.status.code).toEqual("D");
    expect(task.status.name).toEqual("done");
    expect(task.isUpdatable).toBeFalsy();
    expect(task.isDeletable).toBeFalsy();
  });
});
