const { DEL, GET, PATCH, POST, axios, expect } = require("../app");

describe("Task Collections", () => {
  let validateStatus;

  beforeAll(() => {
    validateStatus = axios.defaults.validateStatus;
    axios.defaults.validateStatus = (_status) => true;
  });

  afterAll(() => {
    axios.defaults.validateStatus = validateStatus;
  });

  it("can be read", async () => {
    const { status, data } =
      await GET`/task/Collections/00000000-0000-0000-0000-000000000001`;
    expect(status).to.equal(200);
    expect(data).to.include({
      ID: "00000000-0000-0000-0000-000000000001",
      title: "My Tasks",
      color: null,
      isDefault: true,
    });
  });

  it("can be queried", async () => {
    const { status, data } =
      await GET`/task/Collections?$select=title,color&$orderby=title&$filter=title ne 'Other'`;
    expect(status).to.equal(200);
    expect(data).to.deep.include({
      value: [
        {
          ID: "00000000-0000-0000-0000-000000000003",
          title: "Business",
          color: "FF00FF",
        },
        {
          ID: "00000000-0000-0000-0000-000000000002",
          title: "Life List",
          color: null,
        },
        {
          ID: "00000000-0000-0000-0000-000000000001",
          title: "My Tasks",
          color: null,
        },
      ],
    });
  });

  it("can be created", async () => {
    const { status, data } = await POST("/task/Collections", {
      title: "New list",
      color: "00ff00",
    });
    expect(status).to.equal(201);
    expect(data).to.include({
      title: "New list",
      color: "00FF00",
      isDefault: false,
    });
  });

  it("cannot be created with invalid color", async () => {
    const { status } = await POST("/task/Collections", {
      title: "New list",
      color: "GH01XY",
      isDefault: false,
    });
    expect(status).to.equal(400);
  });

  it("can be created with default flag", async () => {
    let res =
      await GET`/task/Collections?$select=isDefault&$filter=isDefault eq true`;
    expect(res.status).to.equal(200);
    expect(res.data.value.length).to.equal(1);

    res = await POST("/task/Collections", {
      title: "New list",
      color: "00FF00",
      isDefault: true,
    });
    expect(res.status).to.equal(201);
    expect(res.data).to.include({
      title: "New list",
      color: "00FF00",
      isDefault: true,
    });

    res =
      await GET`/task/Collections?$select=isDefault&$filter=isDefault eq true`;
    expect(res.status).to.equal(200);
    expect(res.data.value.length).to.equal(1);
  });

  it("can be updated", async () => {
    const { status, data } = await PATCH(
      "/task/Collections/00000000-0000-0000-0000-000000000003",
      {
        title: "Work List",
        color: "aa0000",
      }
    );
    expect(status).to.equal(200);
    expect(data).to.include({
      ID: "00000000-0000-0000-0000-000000000003",
      title: "Work List",
      color: "AA0000",
      isDefault: false,
    });
  });

  it("cannot be updated with invalid color", async () => {
    const { status } = await PATCH(
      "/task/Collections/00000000-0000-0000-0000-000000000003",
      {
        color: "045WDE",
      }
    );
    expect(status).to.equal(400);
  });

  it("can be updated with default flag", async () => {
    let res =
      await GET`/task/Collections?$select=isDefault&$filter=isDefault eq true`;
    expect(res.status).to.equal(200);
    expect(res.data.value.length).to.equal(1);

    res = await PATCH(
      "/task/Collections/00000000-0000-0000-0000-000000000003",
      {
        isDefault: true,
      }
    );
    expect(res.status).to.equal(200);
    expect(res.data).to.include({
      ID: "00000000-0000-0000-0000-000000000003",
      isDefault: true,
    });

    res =
      await GET`/task/Collections?$select=isDefault&$filter=isDefault eq true`;
    expect(res.status).to.equal(200);
    expect(res.data.value.length).to.equal(1);
  });

  it("can be deleted", async () => {
    const { status } =
      await DEL`/task/Collections/00000000-0000-0000-0000-000000000004`;
    expect(status).to.equal(204);
  });

  it("cannot be deleted with default flag", async () => {
    const { status, data } =
      await GET`/task/Collections?$select=ID&$filter=isDefault eq true`;
    expect(status).to.equal(200);
    expect(data.value.length).to.equal(1);

    const { status: deleteStatus } = await DEL(
      `/task/Collections/${data.value[0].ID}`
    );
    expect(deleteStatus).to.equal(400);
  });

  it("cannot be deleted with tasks assigned", async () => {
    const { status } =
      await DEL`/task/Collections/00000000-0000-0000-0000-000000000002`;
    expect(status).to.equal(400);
  });
});
