import { NextFunction, Request, Response } from "express";
import { ApiError } from "../service/error";

/**
 * Express handler for errors.
 * @param err error that occurred
 * @param req the request
 * @param res the response
 * @param next pointer to next handler
 */
export function handleError(
  err: Error,
  req: Request,
  res: Response,
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  next: NextFunction
): void {
  console.error(err);
  const status = ApiError.is(err) ? err.code : 500;
  res.status(status).render("error", { error: err });
}
