import MockDb from "./db";

export abstract class AbstractMockRepository {
  protected readonly db = MockDb.instance;
}
