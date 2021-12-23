import Task, {
  TaskData,
  TaskDueDate,
  TaskId,
  TaskIsPlannedForMyDay,
} from "../../model/task";
import { TaskRepository } from "../task";
import { AbstractMockRepository } from "./abstract";

export default class TaskMockRepository
  extends AbstractMockRepository
  implements TaskRepository {
  existsById(id: TaskId): Promise<boolean> {
    return Promise.resolve(this.db.tasks.has(id));
  }

  findById(id: TaskId): Promise<Task | undefined> {
    const task = this.db.tasks.get(id);
    return Promise.resolve(task ? this.copyTask(task) : undefined);
  }

  private copyTask(task: Task, id?: number): Task {
    return new Task(id ?? task.id, {
      title: task.title,
      collection: { ...task.collection },
      dueDate: task.dueDate,
      dueTime: task.dueTime,
      status: task.status,
      priority: task.priority,
      isPlannedForMyDay: task.isPlannedForMyDay,
    });
  }

  findAll(): Promise<Task[]> {
    const tasks = Array.from(this.db.tasks.values())
      .sort(this.sortTasks)
      .map((task) => this.copyTask(task));
    return Promise.resolve(tasks);
  }

  private sortTasks(task1: Task, task2: Task): number {
    return task1.id - task2.id;
  }

  findAllByCollection(collection: number): Promise<Task[]> {
    const tasks = Array.from(this.db.tasks.values())
      .filter((task) => task.collection.id === collection)
      .sort(this.sortTasks)
      .map((task) => this.copyTask(task));
    return Promise.resolve(tasks);
  }

  findAllByDueDate(
    dueDate: TaskDueDate,
    isPlannedForMyDay?: TaskIsPlannedForMyDay
  ): Promise<Task[]> {
    const tasks = Array.from(this.db.tasks.values())
      .filter(
        (task) =>
          task.dueDate === dueDate ||
          this.isTaskPlannedForMyDay(task, isPlannedForMyDay)
      )
      .sort(this.sortTasks)
      .map((task) => this.copyTask(task));
    return Promise.resolve(tasks);
  }

  private isTaskPlannedForMyDay(
    task: Task,
    isPlannedForMyDay: TaskIsPlannedForMyDay | undefined
  ): boolean {
    return (
      isPlannedForMyDay !== undefined &&
      task.isPlannedForMyDay === isPlannedForMyDay
    );
  }

  create(data: TaskData): Task {
    return new Task(-1, data);
  }

  save(task: Task): Promise<Task> {
    const id = task.id === -1 ? this.getNextId() : task.id;
    const savedTask = this.copyTask(task, id);
    this.db.tasks.set(savedTask.id, savedTask);
    return Promise.resolve(this.copyTask(savedTask));
  }

  private getNextId(): TaskId {
    const allIds = Array.from(this.db.tasks.keys()).sort(
      (id1, id2) => id2 - id1
    );
    return allIds.length === 0 ? 1 : allIds[0] + 1;
  }

  deleteById(id: TaskId): Promise<void> {
    this.db.tasks.delete(id);
    return Promise.resolve();
  }
}
