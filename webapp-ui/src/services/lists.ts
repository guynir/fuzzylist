import axios from "axios";
import {handleExecution, makeURL} from "@/services/client";

/**
 * Response after a list is created.
 */
export interface CreateListResponse {
    key: string,
    title: string,
    leftToRight: boolean
}

/**
 * Metadata of a list.
 */
export interface ListMetaData {
    key: string,
    title: string,
    leftToRight: boolean
}

/**
 * A single text entry of a list.
 */
export interface EntryResponse {
    id: number,
    index: number,
    text: string
}

/**
 * Response for querying a list.
 */
export interface GetListResponse {
    meta: ListMetaData
    entries: EntryResponse[]
}

/**
 * Create a new list.
 * @param name List name.
 * @param leftToRight Indicates if the list is left-to-right (true) or right-to-left (false).
 * @throws BadRequestException If 'name' does not meet minimum requirements.
 */
export async function createList(name: string, leftToRight: boolean): Promise<CreateListResponse> {
    return handleExecution<CreateListResponse>(axios.post(makeURL(), {
        "title": name,
        "leftToRight": leftToRight
    }));
}

/**
 * Retrieve list details (metadata and items).
 *
 * @param key List identifier.
 * @param limit Maximum number of texts to return. If omitted, server side will limit the number of texts returned.
 * @param ascending Order of appearance - 'true' for ascending order, 'false' - for descending order.
 * @throws NotFoundException If list does not exist.
 */
export async function getList(key: string, limit: number = -1, ascending: boolean = false): Promise<GetListResponse> {
    const config: { params: { [index: string]: any } } = {params: {}};

    if (limit > 0) {
        config.params["limit"] = limit;
    }

    if (!ascending) {
        config.params["order"] = "DESC";
    }

    return handleExecution<GetListResponse>(axios.get(makeURL(key), config));
}

/**
 * Add text to a list.
 * @param key List identifier.
 * @param text Text to add.
 * @throws BadRequestException If text does not meet minimum requirements.
 * @throws NotFoundException If list does not exist.
 */
export function addListItem(key: string, text: string): Promise<any> {
    return handleExecution(axios.post(makeURL(key, "/"), {"text": text},));
}