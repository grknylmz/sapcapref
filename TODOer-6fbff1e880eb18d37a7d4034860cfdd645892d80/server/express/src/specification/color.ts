import AbstractSpecification from "./abstract";
import { StringPatternSpecification } from "./string";

/**
 * This specification checks for well-formed color hex that conforms, for example "03FEB4" or "03feb4"
 */
export class ColorSpecification extends AbstractSpecification<string> {
  isValid(value: string): boolean {
    return new StringPatternSpecification(/^[a-fA-F0-9]{6}$/).isValid(value);
  }
  throwIfInvalid(value: string): void {
    if (!this.isValid(value)) {
      throw new Error(`Value '${value}' is not a color hex'`);
    }
  }
}
