import { ColorSpecification } from "../../src/specification/color";

describe("The ColorSpecification", () => {
  const specification = new ColorSpecification();

  it("is succeeding with valid color", () => {
    expect(specification.isValid("E4C87A")).toBeTruthy();
    expect(() => specification.throwIfInvalid("E4C87A")).not.toThrow();
  });

  it("is succeeding with valid color in lowercase", () => {
    expect(specification.isValid("8dae34")).toBeTruthy();
    expect(() => specification.throwIfInvalid("8dae34")).not.toThrow();
  });

  it("is failing with invalid color", () => {
    expect(specification.isValid("90JHAB")).toBeFalsy();
    expect(() => specification.throwIfInvalid("90JHAB")).toThrow();
  });

  it("is failing with too long value", () => {
    expect(specification.isValid("1234567")).toBeFalsy();
    expect(() => specification.throwIfInvalid("1234567")).toThrow();
  });

  it("is failing with empty string", () => {
    expect(specification.isValid("")).toBeFalsy();
    expect(() => specification.throwIfInvalid("")).toThrow();
  });
});
