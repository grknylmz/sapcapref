import TaskStatus, { TaskStatusCode } from "../model/taskStatus";

/**
 * Repository to read TaskStatus entities
 */
export interface TaskStatusRepository {
  /**
   * Get a single TaskStatus entity by its code
   * @param code a task status code
   */
  findByCode(code: TaskStatusCode): Promise<TaskStatus | undefined>;

  /**
   * Get a single TaskStatus entity by its code
   * @param code a task status code
   */
  getOne(code: TaskStatusCode): Promise<TaskStatus>;
}
