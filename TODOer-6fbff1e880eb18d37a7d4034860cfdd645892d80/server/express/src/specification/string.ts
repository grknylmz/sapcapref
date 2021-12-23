import AbstractSpecification from "./abstract";

abstract class StringSpecification extends AbstractSpecification<string> {}

/**
 * This specification checks whether simple strings are within the boundaries of specified length restrictions.
 */
export class StringLengthSpecification extends StringSpecification {
  constructor(private readonly maxLength: number) {
    super();
  }

  isValid(value: string): boolean {
    return value.length <= this.maxLength;
  }

  throwIfInvalid(value: string): void {
    if (!this.isValid(value)) {
      throw new Error(
        `Value '${value}' is exceeding the maximum length of ${this.maxLength}`
      );
    }
  }
}

/**
 * This specification validates whether simple strings conform to a specified regular expression.
 */
export class StringPatternSpecification extends StringSpecification {
  constructor(private readonly pattern: RegExp) {
    super();
  }

  isValid(value: string): boolean {
    this.pattern.lastIndex = 0;
    return this.pattern.test(value);
  }

  throwIfInvalid(value: string): void {
    if (!this.isValid(value)) {
      throw new Error(
        `Value '${value}' is not matching to pattern '${this.pattern.toString()}'`
      );
    }
  }
}
