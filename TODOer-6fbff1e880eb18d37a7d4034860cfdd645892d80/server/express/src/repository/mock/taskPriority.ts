import TaskPriority, { TaskPriorityCode } from "../../model/taskPriority";
import { TaskPriorityRepository } from "../taskPriority";
import { AbstractMockRepository } from "./abstract";

export default class TaskPriorityMockRepository
  extends AbstractMockRepository
  implements TaskPriorityRepository {
  findByCode(code: TaskPriorityCode): Promise<TaskPriority | undefined> {
    const status = this.db.taskPriorities.get(code);
    return Promise.resolve(status ? this.copyTaskPriority(status) : undefined);
  }

  private copyTaskPriority(status: TaskPriority): TaskPriority {
    return new TaskPriority(status.code, status.name);
  }

  async getOne(code: TaskPriorityCode): Promise<TaskPriority> {
    const priority = await this.findByCode(code);
    if (!priority) {
      throw new Error(`A task priority with code '${code}' does not exist`);
    }
    return priority;
  }
}
