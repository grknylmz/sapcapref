import {
  StringLengthSpecification,
  StringPatternSpecification,
} from "../../src/specification/string";

describe("The StringLengthSpecification", () => {
  const specification = new StringLengthSpecification(10);

  it("is succeeding with empty string", () => {
    expect(specification.isValid("")).toBeTruthy();
    expect(() => specification.throwIfInvalid("")).not.toThrow();
  });

  it("is succeeding with smaller string length", () => {
    expect(specification.isValid("Hello Tom")).toBeTruthy();
    expect(() => specification.throwIfInvalid("Hello Tom")).not.toThrow();
  });

  it("is succeeding with exact string length", () => {
    expect(specification.isValid("Hello Carl")).toBeTruthy();
    expect(() => specification.throwIfInvalid("Hello Carl")).not.toThrow();
  });

  it("is failing with bigger string length", () => {
    expect(specification.isValid("Hello Charlotte")).toBeFalsy();
    expect(() => specification.throwIfInvalid("Hello Charlotte")).toThrow();
  });
});

describe("The StringPatternSpecification", () => {
  const specification = new StringPatternSpecification(/^[a-z]+$/g);

  it("is succeeding with matching string", () => {
    expect(specification.isValid("hello")).toBeTruthy();
    expect(() => specification.throwIfInvalid("hello")).not.toThrow();
  });

  it("is failing with non-matching string", () => {
    expect(specification.isValid("Hello")).toBeFalsy();
    expect(() => specification.throwIfInvalid("Hello")).toThrow();
  });

  it("is failing with empty string", () => {
    expect(specification.isValid("")).toBeFalsy();
    expect(() => specification.throwIfInvalid("")).toThrow();
  });
});

describe("A combined specification", () => {
  const specification = new StringLengthSpecification(6).and(
    new StringPatternSpecification(/^[a-z]+$/g)
  );

  it("is succeeding with shorter, matching string", () => {
    expect(specification.isValid("hello")).toBeTruthy();
    expect(() => specification.throwIfInvalid("hello")).not.toThrow();
  });

  it("is failing with shorter, non-matching string", () => {
    expect(specification.isValid("Hello")).toBeFalsy();
    expect(() => specification.throwIfInvalid("Hello")).toThrow();
  });

  it("is failing with larger, matching string", () => {
    expect(specification.isValid("greetings")).toBeFalsy();
    expect(() => specification.throwIfInvalid("greetings")).toThrow();
  });

  it("is failing with larger, non-matching string", () => {
    expect(specification.isValid("Greetings")).toBeFalsy();
    expect(() => specification.throwIfInvalid("Greetings")).toThrow();
  });
});
