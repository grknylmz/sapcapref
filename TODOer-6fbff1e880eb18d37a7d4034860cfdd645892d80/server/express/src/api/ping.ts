import { Request, Response } from "express";

/**
 * Handler of /ping
 * @param req the request
 * @param res the response
 */
export function ping(req: Request, res: Response): void {
  res.json({
    name: "TODOer API",
    description: "RESTful API for the TODOer app",
    version: "beta",
    uptime: process.uptime(),
  });
}
