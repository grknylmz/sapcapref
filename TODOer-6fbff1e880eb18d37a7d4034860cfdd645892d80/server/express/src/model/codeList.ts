/**
 * Abstract implementation of a code list entity
 */
export default abstract class CodeList<Code, Name> {
  private readonly _code: Code;
  private readonly _name?: Name;

  constructor(code: Code, name?: Name) {
    this._code = code;
    this._name = name;
  }

  get code(): Code {
    return this._code;
  }

  get name(): Name | undefined {
    return this._name;
  }
}
