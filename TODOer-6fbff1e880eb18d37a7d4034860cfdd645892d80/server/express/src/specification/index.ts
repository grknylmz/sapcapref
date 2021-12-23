/**
 * A specification is the implementation of a business rule.
 */
export interface Specification<T> {
  /**
   * Check whether a `value` is fulfilling this specification.
   * @param value a value
   * @returns `true` if specification is fulfilled, `false` otherwise
   */
  isValid(value: T): boolean;

  /**
   * Throw an error if a `value` is not fulfilling this specification.
   * @param value a value
   * @throws value is invalid
   */
  throwIfInvalid(value: T): void;

  /**
   * Combine this specification with another specification by logical AND.
   * @param other another specification
   * @returns the combined specification
   */
  and(other: Specification<T>): Specification<T>;
}
