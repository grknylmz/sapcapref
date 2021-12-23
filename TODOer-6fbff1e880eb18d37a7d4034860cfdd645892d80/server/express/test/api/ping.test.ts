import app from "../../src/app";
import supertest from "supertest";

describe("The API server", () => {
  it("can be pinged", async () => {
    const { body } = await supertest(app).get("/beta/ping").expect(200);
    expect(body).toEqual({
      name: "TODOer API",
      description: "RESTful API for the TODOer app",
      version: "beta",
      uptime: expect.any(Number),
    });
  });
});
