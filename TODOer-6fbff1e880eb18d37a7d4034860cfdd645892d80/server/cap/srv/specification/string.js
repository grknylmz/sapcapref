const { Specification } = require("./abstract");

/**
 * This specification validates whether simple strings conform to a specified regular expression.
 */
class StringPatternSpecification extends Specification {
  constructor(pattern) {
    super();
    this._pattern = pattern;
  }

  isValid(value) {
    this._pattern.lastIndex = 0;
    return this._pattern.test(value);
  }
}

module.exports = { StringPatternSpecification };
