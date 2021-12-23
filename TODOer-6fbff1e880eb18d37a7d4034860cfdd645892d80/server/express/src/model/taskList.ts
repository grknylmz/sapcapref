import { ColorSpecification } from "../specification/color";
import { StringLengthSpecification } from "../specification/string";
import { Entity } from "./abstract";
import { TaskIsDeletable, TaskIsUpdatable } from "./task";

export type TaskListId = number;

export type TaskListTitle = string;

export type TaskListColor = string;

export enum TaskListType {
  Collection = "COLLECTION",
  MyDay = "MY_DAY",
  Tomorrow = "TOMORROW",
}

export type TaskListIsUpdatable = boolean;

export type TaskListIsDeletable = boolean;

export type TaskListIsDefaultCollection = boolean;

export type TaskListRef = {
  id: TaskListId;
  title?: TaskListTitle;
};

export type TaskListData = {
  title?: TaskListTitle;
  color?: TaskListColor;
  type: TaskListType;
  isDefaultCollection?: TaskListIsDefaultCollection;
};

/**
 * Implementation of the TaskList model entity
 */
export default class TaskList extends Entity<TaskListId, TaskListData> {
  private _title?: TaskListTitle;
  private _color?: TaskListColor;
  private readonly _type: TaskListType;
  private _isDefaultCollection: TaskListIsDefaultCollection;

  constructor(id: TaskListId, data: TaskListData) {
    super(id, data);
    this.validateTitle(data.title);
    this._title = data.title;
    this.validateColor(data.color);
    this._color =
      data.type === TaskListType.Collection ? data.color : undefined;
    this._type = data.type;
    this._isDefaultCollection =
      data.type === TaskListType.Collection
        ? data.isDefaultCollection ?? false
        : false;
  }

  get title(): TaskListTitle | undefined {
    return this._title;
  }

  set title(value: TaskListTitle | undefined) {
    if (!this.isUpdatable) {
      return;
    }
    this.validateTitle(value);
    this._title = value;
  }

  private validateTitle(value: TaskListTitle | undefined) {
    if (value) {
      new StringLengthSpecification(40).throwIfInvalid(value);
    }
  }

  get color(): TaskListColor | undefined {
    return this._color;
  }

  set color(value: TaskListColor | undefined) {
    if (!this.isUpdatable) {
      return;
    }
    this.validateColor(value);
    this._color = value?.toUpperCase();
  }

  private validateColor(value: TaskListColor | undefined) {
    if (value) {
      new ColorSpecification().throwIfInvalid(value);
    }
  }

  get type(): TaskListType {
    return this._type;
  }

  get isUpdatable(): TaskIsUpdatable {
    return this.type === TaskListType.Collection;
  }

  get isDeletable(): TaskIsDeletable {
    return this.type === TaskListType.Collection;
  }

  public get isDefaultCollection(): TaskListIsDefaultCollection {
    return this._isDefaultCollection;
  }

  public set isDefaultCollection(value: TaskListIsDefaultCollection) {
    if (!this.isUpdatable) {
      return;
    }
    this._isDefaultCollection = value;
  }

  get ref(): TaskListRef {
    return {
      id: this.id,
      title: this.title,
    };
  }
}
