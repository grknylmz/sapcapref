import { RepositoryFactory } from "..";
import { TaskRepository } from "../task";
import { TaskListRepository } from "../taskList";
import { TaskPriorityRepository } from "../taskPriority";
import { TaskStatusRepository } from "../taskStatus";
import TaskMockRepository from "./task";
import TaskListMockRepository from "./taskList";
import TaskPriorityMockRepository from "./taskPriority";
import TaskStatusMockRepository from "./taskStatus";

export class MockRepositoryFactory implements RepositoryFactory {
  getTaskRepository(): TaskRepository {
    return new TaskMockRepository();
  }

  getTaskStatusRepository(): TaskStatusRepository {
    return new TaskStatusMockRepository();
  }

  getTaskPriorityRepository(): TaskPriorityRepository {
    return new TaskPriorityMockRepository();
  }

  getTaskListRepository(): TaskListRepository {
    return new TaskListMockRepository();
  }
}
