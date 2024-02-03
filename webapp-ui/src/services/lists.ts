import axios, {AxiosResponse} from "axios";

const BASE_URL = "http://localhost/api/v1/list/"

export interface CreateListResponse {
    key: string,
    title: string,
    leftToRight: boolean
}

export interface ListMetaData {
    key: string,
    title: string,
    leftToRight: boolean
}

export interface EntryResponse {
    id: number,
    index: number,
    text: string
}

export interface GetListResponse {
    meta: ListMetaData
    entries: EntryResponse[]
}

export function createList(name: string): Promise<AxiosResponse<CreateListResponse, any>> {
    return axios.post(BASE_URL, {"title": name, "leftToRight": true})
}

export function getList(key: string, limit: number = -1, ascending: boolean = false): Promise<AxiosResponse<GetListResponse, any>> {
    const filterParams: { [index: string]: any } = {};

    if (limit > 0) {
        filterParams["limit"] = limit;
    }

    if (!ascending) {
        filterParams["order"] = "DESC";
    }

    return axios.get(BASE_URL + key, {params: filterParams});
}

export function addListItem(key: string, text: string): Promise<AxiosResponse<any, any>> {

    return axios.post(BASE_URL + key + "/", {"text": text},);
}