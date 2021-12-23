const { Specification } = require("./abstract");
const { StringPatternSpecification } = require("./string");

/**
 * This specification checks for well-formed color hex that conforms, for example "03FEB4" or "03feb4"
 */
class ColorSpecification extends Specification {
  isValid(value) {
    return new StringPatternSpecification(/^[a-fA-F0-9]{6}$/).isValid(value);
  }
}

module.exports = { ColorSpecification };
