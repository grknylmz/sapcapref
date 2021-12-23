import TaskList, { TaskListData, TaskListId } from "../model/taskList";

/**
 * Repository to managed TaskList entities
 */
export interface TaskListRepository {
  /**
   * Get a single task list by ID
   * @param id a task list's ID
   * @returns optional TaskList entity
   */
  findById(id: TaskListId): Promise<TaskList | undefined>;

  /**
   * Get a single task list by ID
   * @param id a task list's ID
   * @returns TaskList entity
   */
  getOne(id: TaskListId): Promise<TaskList>;

  /**
   * Get a list of task lists
   * @returns array of TaskList entities
   */
  findAll(): Promise<TaskList[]>;

  /**
   * Create a new TaskList container entity
   * @param data task list data
   * @returns TaskList entity
   */
  create(data: TaskListData): TaskList;

  /**
   * Save a TaskList in the repository
   * @param taskList TaskList entity
   * @return saved TaskList entity
   */
  save(taskList: TaskList): Promise<TaskList>;
}
