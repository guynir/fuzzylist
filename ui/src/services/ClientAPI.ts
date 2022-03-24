import axios, {AxiosResponse} from "axios";

const client = axios.create({
    baseURL: "/api/v1/",
    headers: {
        "Content-Type": "application/json"
    }
})

export interface HeaderResponse {
    key: string,
    title?: string,
    leftToRight?: boolean
}

export interface EntryResponse {
    id: number
    index: number
    text: string
}

export interface ListDataResponse {
    meta: HeaderResponse,
    entries: Array<EntryResponse>
}

/**
 * Execute API call to create a new list.
 *
 * @param listName String representing list name/title.
 * @param leftToRight Boolean indicating if list should have a left-to-right layout or right-to-left.
 */
export function createList(listName: string, leftToRight: boolean): Promise<AxiosResponse<HeaderResponse>> {
    return client.post("/list/", {
        "title": listName,
        "leftToRight": leftToRight
    });
}

export function getList(listKey: string): Promise<AxiosResponse<ListDataResponse>> {
    return client.get("/list/" + listKey + "?order=DESC")
}

export function createTextEntry(listKey: string, text: string): Promise<AxiosResponse> {
    return client.post("/list/" + listKey + "/", {
        text: text
    })
}