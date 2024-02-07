import {AxiosError, AxiosResponse, HttpStatusCode} from "axios";
import {isDefined} from "@/services/helpers";

const BASE_URL = "/api/v1/list/"


/**
 * Thrown when server side returns HTTP/400 (bad request).
 */
export class BadRequestException {

    /** Message returned from server. */
    public readonly message: string | null

    /**
     * Class constructor.
     *
     * @param message Message returned from server.
     */
    constructor(message: string | null) {
        this.message = message;
    }
}

/**
 * Indicates a resource, such as a list - does not exist.
 */
export class NotFoundException {
}

/**
 * Indicates a server-side error.
 */
export class InternalServerError {
}

export async function handleExecution<T>(promise: Promise<AxiosResponse<T, any>>): Promise<T> {
    try {
        let response: AxiosResponse<T, any> = await promise;
        return handleOKResponse(response);
    } catch (error) {
        handleError(error);
        throw error;
    }
}

export function handleOKResponse<T>(response: AxiosResponse<T, any>) {
    return Promise.resolve<T>(response.data);
}

export function handleError<T>(error: unknown) {
    // If this error is of type "AxiosError", we can extract an error message and return solid type-safe
    // response.
    if (error instanceof AxiosError && error.response) {
        let status: number = error.response.status

        switch (status) {
            case HttpStatusCode.BadRequest:
                let message: string | null = hasPayload(error) ? error.response!.data.message : null;
                throw new BadRequestException(message);

            case HttpStatusCode.NotFound:
                throw new NotFoundException();

            case HttpStatusCode.InternalServerError:
                throw new InternalServerError();

        }
    }

    // If we could not identify the type of error, we're just going to propagate it forward.
    throw error;
}

export function makeURL(...parts: string[]): string {
    let url: string = BASE_URL;
    parts.forEach(part => {
        if (!url.endsWith("/") && !part.startsWith("/")) {
            url = url + "/";
        }

        url = url + part;
    })

    return url;
}

/**
 * Check if a given AxiosError instance has payload.
 *
 * @param err Instance to evaluate.
 * @return true if object contains payload, false if not.
 */
function hasPayload(err: AxiosError<any>): boolean {
    return isDefined(err) && isDefined(err.response) && isDefined(err.response!.data);
}