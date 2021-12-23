import TaskPriority, { TaskPriorityCode } from "../model/taskPriority";

/**
 * Repository to read TaskPriority entities
 */
export interface TaskPriorityRepository {
  /**
   * Get a single TaskPriority entity by its code
   * @param code a task priority code
   */
  findByCode(code: TaskPriorityCode): Promise<TaskPriority | undefined>;

  /**
   * Get a single TaskPriority entity by its code
   * @param code a task priority code
   */
  getOne(code: TaskPriorityCode): Promise<TaskPriority>;
}
