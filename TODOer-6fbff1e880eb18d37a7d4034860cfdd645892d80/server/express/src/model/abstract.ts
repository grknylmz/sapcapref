export abstract class Entity<Id, Data> {
  protected readonly _id: Id;

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  constructor(id: Id, data: Data) {
    this._id = id;
  }

  get id(): Id {
    return this._id;
  }
}
