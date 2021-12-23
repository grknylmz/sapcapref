import TaskList, {
  TaskListData,
  TaskListId,
  TaskListType,
} from "../../model/taskList";
import { TaskListRepository } from "../taskList";
import { AbstractMockRepository } from "./abstract";

export default class TaskListMockRepository
  extends AbstractMockRepository
  implements TaskListRepository {
  findById(id: TaskListId): Promise<TaskList | undefined> {
    const list = this.db.taskLists.get(id);
    return Promise.resolve(list ? this.copyTaskList(list) : undefined);
  }

  private copyTaskList(list: TaskList, id?: number): TaskList {
    return new TaskList(id ?? list.id, {
      title: list.title,
      color: list.color,
      type: list.type,
      isDefaultCollection: list.isDefaultCollection,
    });
  }

  async getOne(id: TaskListId): Promise<TaskList> {
    const list = await this.findById(id);
    if (!list) {
      throw new Error(`A task list with ID ${id} does not exist`);
    }
    return list;
  }

  findAll(): Promise<TaskList[]> {
    const lists = Array.from(this.db.taskLists.values())
      .sort(this.sortTaskLists)
      .map((list) => this.copyTaskList(list));
    return Promise.resolve(lists);
  }

  private sortTaskLists(list1: TaskList, list2: TaskList): number {
    if (list1.type === TaskListType.MyDay) {
      return -1;
    }
    if (list2.type === TaskListType.MyDay) {
      return 1;
    }
    if (list1.type === TaskListType.Tomorrow) {
      return -1;
    }
    if (list2.type === TaskListType.Tomorrow) {
      return 1;
    }
    if (list1.title && list2.title) {
      return list1.title.localeCompare(list2.title);
    }
    if (list1.title) {
      return -1;
    }
    if (list2.title) {
      return 1;
    }
    return list1.id - list2.id;
  }

  create(data: TaskListData): TaskList {
    return new TaskList(-1, data);
  }

  save(list: TaskList): Promise<TaskList> {
    const id = list.id === -1 ? this.getNextId() : list.id;
    const savedList = this.copyTaskList(list, id);
    if (savedList.isDefaultCollection) {
      this.db.taskLists.forEach((l) => (l.isDefaultCollection = false));
    }
    this.db.taskLists.set(id, savedList);
    return Promise.resolve(savedList);
  }

  private getNextId(): TaskListId {
    const allIds = Array.from(this.db.taskLists.keys()).sort(
      (id1, id2) => id2 - id1
    );
    return allIds.length === 0 ? 1 : allIds[0] + 1;
  }
}
