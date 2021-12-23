const { DEL, GET, PATCH, POST, axios, expect } = require("../app");

describe("Task Statuses", () => {
  let validateStatus;

  beforeAll(() => {
    validateStatus = axios.defaults.validateStatus;
    axios.defaults.validateStatus = (_status) => true;
  });

  afterAll(() => {
    axios.defaults.validateStatus = validateStatus;
  });

  it("can be read", async () => {
    const { status, data } = await GET`/task/Statuses/O`;
    expect(status).to.equal(200);
    expect(data).to.include({ code: "O", name: "Open" });
  });

  it("can be queried", async () => {
    const { status, data } = await GET`/task/Statuses`;
    expect(status).to.equal(200);
    expect(data).to.deep.include({
      value: [
        { code: "D", name: "Done" },
        { code: "O", name: "Open" },
        { code: "X", name: "Cancelled" },
      ],
    });
  });

  it("cannot be created", async () => {
    const { status } = await POST("/task/Statuses", {
      code: "P",
      name: "In progress",
    });
    expect(status).to.equal(405);
  });

  it("cannot be updated", async () => {
    const { status } = await PATCH("/task/Statuses/O", {
      name: "Unprocessed",
    });
    expect(status).to.equal(405);
  });

  it("cannot be deleted", async () => {
    const { status } = await DEL`/task/Statuses/O`;
    expect(status).to.equal(405);
  });
});
