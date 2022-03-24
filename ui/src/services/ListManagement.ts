import {createList, createTextEntry, HeaderResponse} from "./ClientAPI"
import {AxiosResponse} from "axios";

const MIN_LIST_NAME_LENGTH: number = 2;
const MAX_LIST_NAME_LENGTH: number = 100;

export class ValidationError {
    errorMessage: string

    constructor(errorMessage: string) {
        this.errorMessage = errorMessage
    }
}

export function createNewList(listName: string, leftToRight: boolean, autoRedirect: boolean = false):
    Promise<AxiosResponse<HeaderResponse> | ValidationError> {
    if (listName === null || listName === undefined || listName.length === 0) {
        return Promise.reject(new ValidationError("Missing list name."))
    }

    if (listName.length < MIN_LIST_NAME_LENGTH) {
        return Promise.reject(new ValidationError("List name too short (must be at least "
            + MIN_LIST_NAME_LENGTH
            + " characters long)."))
    }

    if (listName.length > MAX_LIST_NAME_LENGTH) {
        return Promise.reject(new ValidationError("List name too long (must be at most "
            + MAX_LIST_NAME_LENGTH
            + " characters long)."))
    }

    return createList(listName, leftToRight).then(response => {
        if (autoRedirect) {
            document.location.href = "/lists/" + response.data.key
        }
    }) as Promise<AxiosResponse<HeaderResponse>>
}

export function addText(listKey: string, text: string): Promise<AxiosResponse> {
    return createTextEntry(listKey, text)
}