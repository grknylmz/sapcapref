/**
 * A specialized Error for API errors
 */
export class ApiError extends Error {
  static is(error: Error): error is ApiError {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    return !!(error as any).code;
  }

  static badRequest(error: Error | string): ApiError {
    return this.get(400, error);
  }

  static notFound(error: Error | string): ApiError {
    return this.get(404, error);
  }

  static serverError(error: Error | string): ApiError {
    return this.get(500, error);
  }

  private static get(code: number, error: Error | string): ApiError {
    const message = typeof error === "string" ? error : error.message;
    return new ApiError(code, message);
  }

  readonly code: number;

  constructor(code: number, message: string) {
    super(message);
    this.code = code;
  }
}
