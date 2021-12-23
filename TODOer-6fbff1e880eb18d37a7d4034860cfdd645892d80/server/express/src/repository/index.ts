import { MockRepositoryFactory } from "./mock";
import { TaskRepository } from "./task";
import { TaskListRepository } from "./taskList";
import { TaskPriorityRepository } from "./taskPriority";
import { TaskStatusRepository } from "./taskStatus";

/**
 * Factory to create Repository objects
 */
export interface RepositoryFactory {
  /**
   * Get a repository for Task entities
   * @returns a Task repository
   */
  getTaskRepository(): TaskRepository;

  /**
   * Get a repository for TaskStatus entities
   * @returns a TaskStatus repository
   */
  getTaskStatusRepository(): TaskStatusRepository;

  /**
   * Get a repository for TaskPriority entities
   * @returns a TaskPriority repository
   */
  getTaskPriorityRepository(): TaskPriorityRepository;

  /**
   * Get a repository for TaskList entities
   * @returns a TaskList repository
   */
  getTaskListRepository(): TaskListRepository;
}

export const repositoryFactory = new MockRepositoryFactory();
