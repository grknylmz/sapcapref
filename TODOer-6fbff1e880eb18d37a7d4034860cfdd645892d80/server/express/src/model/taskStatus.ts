import CodeList from "./codeList";

export type TaskStatusCode = string;

export type TaskStatusName = string;

/**
 * Task status code list
 */
export default class TaskStatus extends CodeList<
  TaskStatusCode,
  TaskStatusName
> {
  static get open(): TaskStatusCode {
    return "O";
  }

  static get done(): TaskStatusCode {
    return "D";
  }

  static get cancelled(): TaskStatusCode {
    return "X";
  }
}
