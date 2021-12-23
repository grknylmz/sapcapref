import TaskList, { TaskListType } from "../../src/model/taskList";

describe("Creating a TaskList entity", () => {
  it("succeeds with minimum properties", () => {
    const list = new TaskList(1, { type: TaskListType.Collection });
    expect(list.id).toEqual(1);
    expect(list.title).toBeUndefined();
    expect(list.color).toBeUndefined();
    expect(list.type).toEqual("COLLECTION");
    expect(list.isUpdatable).toBeTruthy();
    expect(list.isDeletable).toBeTruthy();
    expect(list.isDefaultCollection).toBeFalsy();
  });

  it("succeeds with all properties", () => {
    const list = new TaskList(1, {
      title: "new task list",
      color: "8D40FA",
      type: TaskListType.Collection,
      isDefaultCollection: true,
    });
    expect(list.id).toEqual(1);
    expect(list.title).toEqual("new task list");
    expect(list.color).toEqual("8D40FA");
    expect(list.type).toEqual("COLLECTION");
    expect(list.isUpdatable).toBeTruthy();
    expect(list.isDeletable).toBeTruthy();
    expect(list.isDefaultCollection).toBeTruthy();
  });

  it("fails with too long title", () => {
    expect(
      () =>
        new TaskList(1, {
          title:
            "Creating a new task list with a title that is exceeding the 40 characters limit",
          type: TaskListType.Collection,
        })
    ).toThrow();
  });

  it("fails with invalid color", () => {
    expect(
      () =>
        new TaskList(1, {
          color: "ABCDEG",
          type: TaskListType.Collection,
        })
    ).toThrow();
  });

  it("succeeds with My Day view", () => {
    const list = new TaskList(1, {
      title: "My Day",
      color: "8D40FA",
      type: TaskListType.MyDay,
      isDefaultCollection: true,
    });
    expect(list.id).toEqual(1);
    expect(list.title).toEqual("My Day");
    expect(list.color).toBeUndefined();
    expect(list.type).toEqual("MY_DAY");
    expect(list.isUpdatable).toBeFalsy();
    expect(list.isDeletable).toBeFalsy();
    expect(list.isDefaultCollection).toBeFalsy();
  });
});

describe("Updating a TaskList entity", () => {
  it("succeeds for collection", () => {
    const list = new TaskList(1, { type: TaskListType.Collection });
    expect(list.id).toEqual(1);
    expect(list.title).toBeUndefined();
    expect(list.color).toBeUndefined();
    expect(list.type).toEqual("COLLECTION");
    expect(list.isUpdatable).toBeTruthy();
    expect(list.isDeletable).toBeTruthy();
    expect(list.isDefaultCollection).toBeFalsy();

    list.title = "updated title";
    list.color = "fd0325";
    list.isDefaultCollection = true;
    expect(list.id).toEqual(1);
    expect(list.title).toEqual("updated title");
    expect(list.color).toEqual("FD0325");
    expect(list.type).toEqual("COLLECTION");
    expect(list.isUpdatable).toBeTruthy();
    expect(list.isDeletable).toBeTruthy();
    expect(list.isDefaultCollection).toBeTruthy();
  });

  it("fails for task lists other than collections", () => {
    const list = new TaskList(1, {
      title: "My Day",
      type: TaskListType.MyDay,
    });
    expect(list.id).toEqual(1);
    expect(list.title).toEqual("My Day");
    expect(list.color).toBeUndefined();
    expect(list.type).toEqual("MY_DAY");
    expect(list.isUpdatable).toBeFalsy();
    expect(list.isDeletable).toBeFalsy();
    expect(list.isDefaultCollection).toBeFalsy();

    list.title = "updated title";
    list.color = "034259";
    list.isDefaultCollection = true;
    expect(list.id).toEqual(1);
    expect(list.title).toEqual("My Day");
    expect(list.color).toBeUndefined();
    expect(list.type).toEqual("MY_DAY");
    expect(list.isUpdatable).toBeFalsy();
    expect(list.isDeletable).toBeFalsy();
    expect(list.isDefaultCollection).toBeFalsy();
  });

  it("fails with invalid color", () => {
    const list = new TaskList(1, { type: TaskListType.Collection });
    expect(() => (list.color = "not_a_color")).toThrow();
  });
});
