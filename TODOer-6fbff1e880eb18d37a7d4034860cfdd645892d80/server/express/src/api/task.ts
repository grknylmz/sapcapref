import { NextFunction, Request, Response } from "express";
import { TaskId } from "../model/task";
import { ApiError } from "../service/error";
import TaskService from "../service/task";

/**
 * Handler of GET /Tasks/{id}
 * @param req the request
 * @param res the response
 * @param next pointer to next handler
 */
export async function getTask(
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> {
  const taskId = Number(req.params.id);
  if (!taskId) {
    next(ApiError.badRequest("Task ID is required"));
    return;
  }

  try {
    const service = new TaskService();
    const task = await service.getTask(taskId);
    res.json(task);
    next();
  } catch (err) {
    next(err);
  }
}

/**
 * Handler of GET /Tasks
 * @param req the request
 * @param res the response
 * @param next pointer to next handler
 */
export async function getTasks(
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> {
  const query = req.query;
  const collection = query.collection ? Number(query.collection) : undefined;

  try {
    const service = new TaskService();
    const tasks = await service.findTasks({ collection });
    res.json({ value: tasks });
    next();
  } catch (err) {
    next(err);
  }
}

/**
 * Handler of POST /Tasks
 * @param req the request
 * @param res the response
 * @param next pointer to next handler
 */
export async function createTask(
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> {
  const body = req.body;
  if (!body.collection) {
    next(ApiError.badRequest("Collection is required"));
    return;
  }

  try {
    const service = new TaskService();
    const task = await service.createTask({
      title: body.title,
      collection: body.collection,
      dueDate: body.dueDate,
      dueTime: body.dueTime,
      priority: body.priority,
      isPlannedForMyDay: body.isPlannedForMyDay,
    });
    res.status(201).json(task);
    next();
  } catch (err) {
    next(err);
  }
}

/**
 * Handler of PATCH /Tasks/{id}
 * @param req the request
 * @param res the response
 * @param next pointer to next handler
 */
export async function updateTask(
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> {
  const taskId = Number(req.params.id);
  if (!taskId) {
    next(ApiError.badRequest("Task ID is required"));
    return;
  }
  const body = req.body;

  try {
    const service = new TaskService();
    const task = await service.updateTask(taskId, {
      title: body.title,
      collection: body.collection,
      dueDate: body.dueDate,
      dueTime: body.dueTime,
      isPlannedForMyDay: body.isPlannedForMyDay,
    });
    res.json(task);
    next();
  } catch (err) {
    next(err);
  }
}

/**
 * Handler of DELETE /Tasks/{id}
 * @param req the request
 * @param res the response
 * @param next pointer to next handler
 */
export async function deleteTask(
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> {
  const taskId = Number(req.params.id);
  if (!taskId) {
    next(ApiError.badRequest("Task ID is required"));
    return;
  }

  try {
    const service = new TaskService();
    await service.deleteTask(taskId);
    res.status(204).send();
    next();
  } catch (err) {
    next(err);
  }
}

/**
 * Handler of POST /Tasks/{id}/setToDone
 * @param req the request
 * @param res the response
 * @param next pointer to next handler
 */
export async function setTaskToDone(
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> {
  updateTaskStatus(req, res, next, (id, service) => service.setTaskToDone(id));
}

type UpdateTaskStatusFn = (id: TaskId, service: TaskService) => Promise<void>;

async function updateTaskStatus(
  req: Request,
  res: Response,
  next: NextFunction,
  updateStatus: UpdateTaskStatusFn
): Promise<void> {
  const taskId = Number(req.params.id);
  if (!taskId) {
    next(ApiError.badRequest("Task ID is required"));
  }

  try {
    await updateStatus(taskId, new TaskService());
    res.status(204).send();
    next();
  } catch (err) {
    next(err);
  }
}

/**
 * Handler of POST /Tasks/{id}/cancel
 * @param req the request
 * @param res the response
 * @param next pointer to next handler
 */
export async function cancelTask(
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> {
  updateTaskStatus(req, res, next, (id, service) => service.cancelTask(id));
}

/**
 * Handler of POST /Tasks/{id}/reopen
 * @param req the request
 * @param res the response
 * @param next pointer to next handler
 */
export async function reopenTask(
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> {
  updateTaskStatus(req, res, next, (id, service) => service.reopenTask(id));
}
