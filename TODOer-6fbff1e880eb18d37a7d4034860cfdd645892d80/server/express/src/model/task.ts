import { DateSpecification, TimeSpecification } from "../specification/date";
import { StringLengthSpecification } from "../specification/string";
import { Entity } from "./abstract";
import { TaskListRef } from "./taskList";
import TaskPriority from "./taskPriority";
import TaskStatus from "./taskStatus";

export type TaskId = number;

export type TaskTitle = string;

export type TaskDueDate = string;

export type TaskDueTime = string;

export type TaskIsUpdatable = boolean;

export type TaskIsDeletable = boolean;

export type TaskIsPlannedForMyDay = boolean;

export type TaskData = {
  title?: TaskTitle;
  collection: TaskListRef;
  dueDate?: TaskDueDate;
  dueTime?: TaskDueTime;
  status: TaskStatus;
  priority?: TaskPriority;
  isPlannedForMyDay?: TaskIsPlannedForMyDay;
};

/**
 * Implementation of the Task model entity
 */
export default class Task extends Entity<TaskId, TaskData> {
  private _title?: TaskTitle;
  private _collection: TaskListRef;
  private _dueDate?: TaskDueDate;
  private _dueTime?: TaskDueTime;
  private _status: TaskStatus;
  private _priority?: TaskPriority;
  private _isPlannedForMyDay: TaskIsPlannedForMyDay;

  constructor(id: TaskId, data: TaskData) {
    super(id, data);
    this.validateTitle(data.title);
    this._title = data.title;
    this._collection = data.collection;
    this.validateDueDate(data.dueDate);
    this._dueDate = data.dueDate;
    this.validateDueTime(data.dueTime);
    this._dueTime = data.dueTime;
    this._status = data.status;
    this._priority = data.priority;
    this._isPlannedForMyDay = data.isPlannedForMyDay ?? false;
  }

  get title(): TaskTitle | undefined {
    return this._title;
  }

  set title(value: TaskTitle | undefined) {
    if (!this.isUpdatable) {
      return;
    }
    this.validateTitle(value);
    this._title = value;
  }

  private validateTitle(value: TaskTitle | undefined) {
    if (value) {
      new StringLengthSpecification(40).throwIfInvalid(value);
    }
  }

  get collection(): TaskListRef {
    return this._collection;
  }

  set collection(value: TaskListRef) {
    if (!this.isUpdatable) {
      return;
    }
    this._collection = value;
  }

  get dueDate(): TaskDueDate | undefined {
    return this._dueDate;
  }

  set dueDate(value: TaskDueDate | undefined) {
    if (!this.isUpdatable) {
      return;
    }
    this.validateDueDate(value);
    if (!value) {
      this.dueTime = undefined;
    }
    this._dueDate = value;
  }

  private validateDueDate(value: TaskDueDate | undefined) {
    if (value) {
      new DateSpecification().throwIfInvalid(value);
    }
  }

  get dueTime(): TaskDueTime | undefined {
    return this._dueTime;
  }

  set dueTime(value: TaskDueTime | undefined) {
    if (!this.isUpdatable) {
      return;
    }
    this.validateDueTime(value);
    this._dueTime = value;
  }

  private validateDueTime(value: string | undefined) {
    if (value) {
      new TimeSpecification().throwIfInvalid(value);
      if (!this.dueDate) {
        throw new Error("Due date must not be null, if due time is not null");
      }
    }
  }

  get status(): TaskStatus {
    return this._status;
  }

  set status(value: TaskStatus) {
    this._status = value;
  }

  get isUpdatable(): TaskIsUpdatable {
    return this.status.code === TaskStatus.open;
  }

  get isDeletable(): TaskIsDeletable {
    return this.isUpdatable;
  }

  get priority(): TaskPriority | undefined {
    return this._priority;
  }

  set priority(value: TaskPriority | undefined) {
    if (!this.isUpdatable) {
      return;
    }
    this._priority = value;
  }

  get isPlannedForMyDay(): TaskIsPlannedForMyDay {
    return this._isPlannedForMyDay;
  }

  set isPlannedForMyDay(value: TaskIsPlannedForMyDay) {
    if (!this.isUpdatable) {
      return;
    }
    this._isPlannedForMyDay = value;
  }
}
