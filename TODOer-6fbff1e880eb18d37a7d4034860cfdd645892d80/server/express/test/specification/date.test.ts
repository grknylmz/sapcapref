import {
  DateSpecification,
  TimeSpecification,
} from "../../src/specification/date";

describe("The DateSpecification", () => {
  const specification = new DateSpecification();

  it("is succeeding with valid date", () => {
    expect(specification.isValid("2021-03-07")).toBeTruthy();
    expect(() => specification.throwIfInvalid("2021-03-07")).not.toThrow();
  });

  it("is failing with invalid date", () => {
    expect(specification.isValid("2021-15-07")).toBeFalsy();
    expect(() => specification.throwIfInvalid("2021-15-07")).toThrow();
  });

  it("is failing with date-time", () => {
    expect(specification.isValid("2021-03-07T16:17:00")).toBeFalsy();
    expect(() => specification.throwIfInvalid("2021-03-07T16:17:00")).toThrow();
  });

  it("is failing with empty string", () => {
    expect(specification.isValid("")).toBeFalsy();
    expect(() => specification.throwIfInvalid("")).toThrow();
  });
});

describe("The TimeSpecification", () => {
  const specification = new TimeSpecification();

  it("is succeeding with valid time", () => {
    expect(specification.isValid("16:17:00")).toBeTruthy();
    expect(() => specification.throwIfInvalid("16:17:00")).not.toThrow();
  });

  it("is failing with invalid time", () => {
    expect(specification.isValid("25:88:00")).toBeFalsy();
    expect(() => specification.throwIfInvalid("25:88:00")).toThrow();
  });

  it("is failing with date-time", () => {
    expect(specification.isValid("2021-03-07T16:17:00")).toBeFalsy();
    expect(() => specification.throwIfInvalid("2021-03-07T16:17:00")).toThrow();
  });

  it("is failing with empty string", () => {
    expect(specification.isValid("")).toBeFalsy();
    expect(() => specification.throwIfInvalid("")).toThrow();
  });
});
