import CodeList from "./codeList";

export type TaskPriorityCode = number;

export type TaskPriorityName = string;

/**
 * Task priority code list
 */
export default class TaskPriority extends CodeList<
  TaskPriorityCode,
  TaskPriorityName
> {}
