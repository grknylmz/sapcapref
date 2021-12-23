/**
 * Abstract implementation of a specification
 */
class Specification {
  isValid(value) {
    return false;
  }

  and(other) {
    return new AndSpecification(this, other);
  }
}

class AndSpecification extends Specification {
  constructor(left, right) {
    super();
    this._left = left;
    this._right = right;
  }

  isValid(value) {
    return this._left.isValid(value) && this._right.isValid(value);
  }
}

module.exports = { Specification, AndSpecification };
