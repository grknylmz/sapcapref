const { DEL, GET, PATCH, POST, axios, expect } = require("../app");

describe("Tasks", () => {
  let validateStatus;

  beforeAll(() => {
    validateStatus = axios.defaults.validateStatus;
    axios.defaults.validateStatus = (_status) => true;
  });

  afterAll(() => {
    axios.defaults.validateStatus = validateStatus;
  });

  it("can be read", async () => {
    const {
      status,
      data,
    } = await GET`/task/Tasks/00000000-0000-0000-0000-00000000000C`;
    expect(status).to.equal(200);
    expect(data).to.deep.include({
      ID: "00000000-0000-0000-0000-00000000000C",
      title: "What is the meaning of life?",
      collection: { ID: "00000000-0000-0000-0000-000000000002" },
      status: { code: "D" },
      priority: null,
      dueDate: null,
      dueTime: null,
      isPlannedForMyDay: false,
    });
  });

  it("can be queried by collection", async () => {
    const {
      status,
      data,
    } = await GET`/task/Tasks?$filter=collection/ID eq 00000000-0000-0000-0000-000000000002`;
    expect(status).to.equal(200);
    expect(data).to.deep.include({
      value: [
        {
          ID: "00000000-0000-0000-0000-00000000000C",
          title: "What is the meaning of life?",
          collection: { ID: "00000000-0000-0000-0000-000000000002" },
          status: { code: "D" },
          priority: null,
          dueDate: null,
          dueTime: null,
          isPlannedForMyDay: false,
        },
      ],
    });
  });

  it("can be queried by due date and planned for my day flag", async () => {
    const {
      status,
      data,
    } = await GET`/task/Tasks?$select=title&$filter=status/code eq 'O' and (dueDate le 2021-05-31 or isPlannedForMyDay eq true)&$orderby=title`;
    expect(status).to.equal(200);
    expect(data).to.deep.include({
      value: [
        {
          ID: "00000000-0000-0000-0000-00000000000B",
          title: "Groceries shopping",
        },
        {
          ID: "00000000-0000-0000-0000-00000000000A",
          title: "Tax declaration",
        },
        {
          ID: "00000000-0000-0000-0000-00000000000D",
          title: "Very important presentation",
        },
      ],
    });
  });

  it("can be created", async () => {
    const { status, data } = await POST("/task/Tasks", {
      title: "New task",
      collection: { ID: "00000000-0000-0000-0000-000000000001" },
      priority: { code: 3 },
      dueDate: "2021-04-27",
    });
    expect(status).to.equal(201);
    expect(data).to.deep.include({
      title: "New task",
      collection: { ID: "00000000-0000-0000-0000-000000000001" },
      status: { code: "O" },
      priority: { code: 3 },
      dueDate: "2021-04-27",
      dueTime: null,
      isPlannedForMyDay: false,
    });
  });

  it("cannot be created with due time but without date", async () => {
    const { status } = await POST("/task/Tasks", {
      collection: { ID: "00000000-0000-0000-0000-000000000001" },
      dueTime: "13:15:00",
    });
    expect(status).to.equal(400);
  });

  it("can be updated", async () => {
    const { status, data } = await PATCH(
      "/task/Tasks/00000000-0000-0000-0000-00000000000A",
      {
        priority: { code: 3 },
        dueTime: "13:15:00",
        isPlannedForMyDay: true,
      }
    );
    expect(status).to.equal(200);
    expect(data).to.deep.include({
      ID: "00000000-0000-0000-0000-00000000000A",
      title: "Tax declaration",
      collection: { ID: "00000000-0000-0000-0000-000000000001" },
      status: { code: "O" },
      priority: { code: 3 },
      dueDate: "2021-05-31",
      dueTime: "13:15:00",
      isPlannedForMyDay: true,
    });
  });

  it("cannot be updated with due time but without date", async () => {
    const { status } = await PATCH(
      "/task/Tasks/00000000-0000-0000-0000-00000000000B",
      {
        dueDate: null,
        dueTime: "13:15:00",
      }
    );
    expect(status).to.equal(400);
  });

  it("cannot be updated without due date when time was set", async () => {
    const { status } = await PATCH(
      "/task/Tasks/00000000-0000-0000-0000-00000000000D",
      {
        dueDate: null,
      }
    );
    expect(status).to.equal(400);
  });

  it("cannot be updated with due time when date was not set", async () => {
    const { status } = await PATCH(
      "/task/Tasks/00000000-0000-0000-0000-00000000000B",
      {
        dueTime: "13:15:00",
      }
    );
    expect(status).to.equal(400);
  });

  it("cannot updated when done", async () => {
    const { status } = await PATCH(
      "/task/Tasks/00000000-0000-0000-0000-00000000000C",
      {
        title: "new title",
      }
    );
    expect(status).to.equal(400);
  });

  it("can be deleted", async () => {
    const {
      status,
    } = await DEL`/task/Tasks/00000000-0000-0000-0000-00000000000E`;
    expect(status).to.equal(204);
  });

  it("cannot be deleted when done", async () => {
    const {
      status,
    } = await DEL`/task/Tasks/00000000-0000-0000-0000-00000000000C`;
    expect(status).to.equal(400);
  });

  it("can be set to done", async () => {
    const { status } = await POST(
      "/task/Tasks/00000000-0000-0000-0000-00000000000F/TaskService.setToDone",
      {}
    );
    expect(status).to.equal(204);
    const {
      data,
    } = await GET`/task/Tasks/00000000-0000-0000-0000-00000000000F`;
    expect(data).to.deep.include({
      ID: "00000000-0000-0000-0000-00000000000F",
      status: { code: "D" },
    });
  });

  it("can be cancelled", async () => {
    const { status } = await POST(
      "/task/Tasks/00000000-0000-0000-0000-0000000000AA/TaskService.cancel",
      {}
    );
    expect(status).to.equal(204);
    const {
      data,
    } = await GET`/task/Tasks/00000000-0000-0000-0000-0000000000AA`;
    expect(data).to.deep.include({
      ID: "00000000-0000-0000-0000-0000000000AA",
      status: { code: "X" },
    });
  });

  it("can be reopened", async () => {
    const { status } = await POST(
      "/task/Tasks/00000000-0000-0000-0000-0000000000AB/TaskService.reopen",
      {}
    );
    expect(status).to.equal(204);
    const {
      data,
    } = await GET`/task/Tasks/00000000-0000-0000-0000-0000000000AB`;
    expect(data).to.deep.include({
      ID: "00000000-0000-0000-0000-0000000000AB",
      status: { code: "O" },
    });
  });
});
