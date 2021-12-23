import Task, {
  TaskData,
  TaskDueDate,
  TaskId,
  TaskIsPlannedForMyDay,
} from "../model/task";
import { TaskListId } from "../model/taskList";

/**
 * Repository to managed Task entities
 */
export interface TaskRepository {
  /**
   * Check whether a task for a given ID exists in this repository
   * @param id a task's ID
   * @returns true if a task exists, false otherwise
   */
  existsById(id: TaskId): Promise<boolean>;

  /**
   * Get a single task by ID
   * @param id a task's ID
   * @returns optional Task entity
   */
  findById(id: TaskId): Promise<Task | undefined>;

  /**
   * Get all tasks
   * @returns list of Task entities
   */
  findAll(): Promise<Task[]>;

  /**
   * Get all tasks for a specific collection
   * @param collection ID of a TaskList
   * @returns list of Task entities
   */
  findAllByCollection(collection: TaskListId): Promise<Task[]>;

  /**
   * Get all tasks with a specific due date
   * @param dueDate due date
   * @param isPlannedForMyDay optional flag
   * @returns list of Task entities
   */
  findAllByDueDate(
    dueDate: TaskDueDate,
    isPlannedForMyDay?: TaskIsPlannedForMyDay
  ): Promise<Task[]>;

  /**
   * Create a new Task entity container with specified data
   * @param data task data
   * @returns Task entity
   */
  create(data: TaskData): Task;

  /**
   * Save a Task in the repository
   * @param task Task entity
   * @return saved Task entity
   */
  save(task: Task): Promise<Task>;

  /**
   * Delete a Task by its ID from this repository
   * @param id a task's ID
   */
  deleteById(id: TaskId): Promise<void>;
}
