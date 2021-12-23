import TaskStatus, { TaskStatusCode } from "../../model/taskStatus";
import { TaskStatusRepository } from "../taskStatus";
import { AbstractMockRepository } from "./abstract";

export default class TaskStatusMockRepository
  extends AbstractMockRepository
  implements TaskStatusRepository {
  findByCode(code: TaskStatusCode): Promise<TaskStatus | undefined> {
    const status = this.db.taskStatuses.get(code);
    return Promise.resolve(status ? this.copyTaskStatus(status) : undefined);
  }

  private copyTaskStatus(status: TaskStatus): TaskStatus {
    return new TaskStatus(status.code, status.name);
  }

  async getOne(code: TaskStatusCode): Promise<TaskStatus> {
    const status = await this.findByCode(code);
    if (!status) {
      throw new Error(`A task status with code '${code}' does not exist`);
    }
    return status;
  }
}
