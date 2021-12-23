import { Specification } from ".";

/**
 * Abstract implementation of a specification
 */
export default abstract class AbstractSpecification<T>
  implements Specification<T> {
  abstract isValid(value: T): boolean;

  abstract throwIfInvalid(value: T): void;

  and(other: Specification<T>): Specification<T> {
    return new AndSpecification(this, other);
  }
}

class AndSpecification<T> extends AbstractSpecification<T> {
  constructor(
    private readonly left: Specification<T>,
    private readonly right: Specification<T>
  ) {
    super();
  }

  isValid(value: T): boolean {
    return this.left.isValid(value) && this.right.isValid(value);
  }

  throwIfInvalid(value: T): void {
    this.left.throwIfInvalid(value);
    this.right.throwIfInvalid(value);
  }
}
