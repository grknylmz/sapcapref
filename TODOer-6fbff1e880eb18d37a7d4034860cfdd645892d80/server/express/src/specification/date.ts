import AbstractSpecification from "./abstract";
import { StringPatternSpecification } from "./string";

/**
 * This specification checks for well-formed date values that conform to the pattern `YYYY-MM-DD`, for example `2021-11-07`.
 */
export class DateSpecification extends AbstractSpecification<string> {
  isValid(value: string): boolean {
    return new StringPatternSpecification(
      /^\d{4}\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])$/
    ).isValid(value);
  }

  throwIfInvalid(value: string): void {
    if (!this.isValid(value)) {
      throw new Error(`Value '${value}' is not a date'`);
    }
  }
}

/**
 * This specification checks for well-formed time values that conform to the pattern `HH:mm:ss`, for example `18:08:32`.
 */
export class TimeSpecification extends AbstractSpecification<string> {
  isValid(value: string): boolean {
    return new StringPatternSpecification(
      /^(?:[01]\d|2[0123]):(?:[012345]\d):(?:[012345]\d)$/
    ).isValid(value);
  }
  throwIfInvalid(value: string): void {
    if (!this.isValid(value)) {
      throw new Error(`Value '${value}' is not a time'`);
    }
  }
}
