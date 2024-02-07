'use client';

import React, {KeyboardEventHandler, useEffect, useRef, useState} from "react";
import {addListItem, EntryResponse, getList, GetListResponse} from "@/services/lists";
import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import "primeicons/primeicons.css";
import {TypeHandler} from "@/services/typehandler";
import {BadRequestException, NotFoundException} from "@/services/client";
import {hasText} from "@/services/helpers";

export default function ListPage({params}: { params: { key: string } }) {
    const listKey: string = params.key;

    const [loadMessage, setLoadMessage] = useState<string | null>("Loading ...");
    const [title, setTitle] = useState("")
    const [entries, setEntries] = useState<EntryResponse[]>([]);
    const [errorMessage, setErrorMessage] = useState<string | null>(null)

    const fetchList = (): void => {
        getList(listKey, 25, false)
            .then((response: GetListResponse) => {
                setTitle(response.meta.title);
                setEntries(response.entries);
                setLoadMessage(null);
            })
            .catch(error => {
                TypeHandler.handle(error)
                    .on(NotFoundException, () => setLoadMessage("List does not exist."))
                    .else(() => {
                        throw error
                    });
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
            return;
        }

        let text: string | null | undefined = inputRef.current.value;

        // If user did not provide any text -- ignore request to add text item. Just exit.
        if (!hasText(text)) {
            inputRef.current.focus();
            return;
        }

        addListItem(listKey, text)
            .then(() => {
                setErrorMessage("");
                fetchList();
            })
            .catch(error => {
                TypeHandler.handle(error)
                    .on(BadRequestException, (ex: BadRequestException) => setErrorMessage(ex.message))
                    .else(() => setErrorMessage("Unexpected error."))
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
                    <div className="flex flex-row">
                        <div className="py-3 px-3">
                            <div className="pi pi-arrow-circle-left" onClick={() => document.location.href = "/"}></div>
                        </div>
                        <div className="w-full">
                            <div className="flex flex-col content-left text-left w-full py-0 font-secondary-header">
                                {title}
                            </div>
                            <div className="w-full">
                                <InputText id="listName" type="text"
                                           className="border-solid border-1 h-8 p-0 border-gray-200 rounded-sm w-96"
                                           onKeyDown={handleKey}
                                           ref={inputRef}/>
                                <Button className="mx-3 h-8 border-2 border-gray-200" label="Add" onClick={handleClick}
                                        ref={buttonRef}/>

                                <div className="py-2 mb-4 text-sm text-red-600 rounded-lg" role="alert">
                                    <div className="text-lg h-5">{errorMessage}</div>
                                </div>

                                {entries.map((v: EntryResponse) => {
                                    return <div key={"text-" + v.id}>
                                        <span className="pr-3">{v.index}.</span>
                                        <span>{v.text}</span>
                                    </div>
                                })}
                            </div>
                        </div>
                    </div>
                    : <span>{loadMessage}</span>
                }
            </div>
        </main>
    )
}