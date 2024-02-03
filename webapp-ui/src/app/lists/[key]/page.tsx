'use client';

import React, {KeyboardEventHandler, useEffect, useRef, useState} from "react";
import {addListItem, EntryResponse, getList, GetListResponse} from "@/services/lists";
import {AxiosResponse} from "axios";
import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";

export default function ListPage({params}: { params: { key: string } }) {
    const listKey: string = params.key;

    const [loadMessage, setLoadMessage] = useState<string | null>("Loading ...");
    const [title, setTitle] = useState("")
    const [entries, setEntries] = useState<EntryResponse[]>([]);

    const fetchList = (): void => {
        getList(listKey, 25, false)
            .then((response: AxiosResponse<GetListResponse, any>) => {
                setTitle(response.data.meta.title);
                setEntries(response.data.entries);
                setLoadMessage(null);
            })
            .catch(error => {
                alert(error);
            })
    }

    // Upon page load -- fetch list data.
    useEffect(() => {
        fetchList();
    }, []);

    const inputRef = useRef<HTMLInputElement>(null);
    const buttonRef = useRef<Button>(null);

    const handleClick = (): void => {
        if (!inputRef.current) {
            return
        }

        addListItem(listKey, inputRef.current.value)
            .then(() => {
                fetchList();
            })
            .catch(() => {
                alert("ERROR!");
            })

        inputRef.current.value = "";
        inputRef.current.focus();
    }

    const handleKey: KeyboardEventHandler = (source: React.KeyboardEvent) => {
        if (source.key == "Enter") {
            handleClick();
        }
    }

    return (
        <main className="flex flex-col min-h-screen items-center justify-between p-24">
            <div className="w-full items-center max-w-2xl default-font">
                {loadMessage == null ?
                    <div>
                        <h1 className="flex flex-col content-left text-left w-full py-4 font-secondary-header">
                            {title}
                        </h1>
                        <div>
                            <InputText id="listName" type="text"
                                       className="border-solid border-1 h-8 p-0 border-gray-200 rounded-sm"
                                       onKeyDown={handleKey}
                                       ref={inputRef}/>
                            <Button className="mx-3 h-8 border-2 border-gray-200" label="Add" onClick={handleClick}
                                    ref={buttonRef}/>

                            {entries.map((v: EntryResponse) => {
                                return <div key={"text-" + v.id}>
                                    <span className="pr-3">{v.index}.</span>
                                    <span>{v.text}</span>
                                </div>
                            })}
                        </div>
                    </div>
                    : <span>{loadMessage}</span>
                }
            </div>
        </main>
    )
}