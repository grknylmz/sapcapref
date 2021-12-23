const { DEL, GET, PATCH, POST, axios, expect } = require("../app");

describe("Task Priorities", () => {
  let validateStatus;

  beforeAll(() => {
    validateStatus = axios.defaults.validateStatus;
    axios.defaults.validateStatus = (_status) => true;
  });

  afterAll(() => {
    axios.defaults.validateStatus = validateStatus;
  });

  it("can be read", async () => {
    const { status, data } = await GET`/task/Priorities/1`;
    expect(status).to.equal(200);
    expect(data).to.include({ code: 1, name: "High" });
  });

  it("can be queried", async () => {
    const { status, data } = await GET`/task/Priorities`;
    expect(status).to.equal(200);
    expect(data).to.deep.include({
      value: [
        { code: 1, name: "High" },
        { code: 3, name: "Moderate" },
        { code: 5, name: "Low" },
      ],
    });
  });

  it("cannot be created", async () => {
    const { status } = await POST("/task/Priorities", {
      code: 4,
      name: "Nice-to-have",
    });
    expect(status).to.equal(405);
  });

  it("cannot be updated", async () => {
    const { status } = await PATCH("/task/Priorities/1", {
      name: "Very high",
    });
    expect(status).to.equal(405);
  });

  it("cannot be deleted", async () => {
    const { status } = await DEL`/task/Priorities/1`;
    expect(status).to.equal(405);
  });
});
