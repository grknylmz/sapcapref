import Task from "../../model/task";
import TaskList, { TaskListId, TaskListType } from "../../model/taskList";
import TaskPriority, { TaskPriorityCode } from "../../model/taskPriority";
import TaskStatus, { TaskStatusCode } from "../../model/taskStatus";

export default class MockDb {
  private static singleton: MockDb;

  static get instance(): MockDb {
    if (!this.singleton) {
      this.singleton = new MockDb();
      this.singleton.initialize();
    }
    return this.singleton;
  }

  private _tasks: Map<TaskListId, Task>;
  private _taskStatuses: Map<TaskStatusCode, TaskStatus>;
  private _taskPriorities: Map<TaskPriorityCode, TaskPriority>;
  private _taskLists: Map<TaskListId, TaskList>;

  private constructor() {
    this._tasks = new Map();
    this._taskStatuses = new Map();
    this._taskPriorities = new Map();
    this._taskLists = new Map();
  }

  get tasks(): Map<TaskListId, Task> {
    return this._tasks;
  }

  get taskStatuses(): Map<TaskStatusCode, TaskStatus> {
    return this._taskStatuses;
  }

  get taskPriorities(): Map<TaskPriorityCode, TaskPriority> {
    return this._taskPriorities;
  }

  get taskLists(): Map<TaskListId, TaskList> {
    return this._taskLists;
  }

  reset(): void {
    this.initialize();
  }

  private initialize(): void {
    const openStatus = new TaskStatus("O", "open");
    const doneStatus = new TaskStatus("D", "done");
    const cancelledStatus = new TaskStatus("X", "cancelled");
    this.taskStatuses.clear();
    this.taskStatuses
      .set("O", openStatus)
      .set("D", doneStatus)
      .set("X", cancelledStatus);

    const highPriority = new TaskPriority(1, "high");
    const mediumPriority = new TaskPriority(3, "medium");
    const lowPriority = new TaskPriority(5, "low");
    this.taskPriorities.clear();
    this.taskPriorities
      .set(1, highPriority)
      .set(3, mediumPriority)
      .set(5, lowPriority);

    const myTasks = new TaskList(1, {
      title: "My Tasks",
      color: "0000FF",
      type: TaskListType.Collection,
      isDefaultCollection: true,
    });
    const lifeList = new TaskList(42, { type: TaskListType.Collection });
    this.taskLists.clear();
    this.taskLists
      .set(2, new TaskList(2, { title: "My Day", type: TaskListType.MyDay }))
      .set(
        3,
        new TaskList(3, { title: "Tomorrow", type: TaskListType.Tomorrow })
      )
      .set(1, myTasks)
      .set(42, lifeList);

    this.tasks.clear();
    this.tasks
      .set(
        1,
        new Task(1, {
          title: "Tax declaration",
          collection: myTasks.ref,
          dueDate: "2021-05-31",
          status: openStatus,
          priority: lowPriority,
        })
      )
      .set(
        2,
        new Task(2, {
          title: "Groceries shopping",
          collection: myTasks.ref,
          status: openStatus,
          priority: lowPriority,
          isPlannedForMyDay: true,
        })
      )
      .set(
        42,
        new Task(42, {
          title: "What is the meaning of life?",
          collection: lifeList.ref,
          status: doneStatus,
        })
      );
  }
}
