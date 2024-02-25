'use client';

import React, {useEffect, useRef, useState} from "react";
import {addListItem, EntryResponse, getList, GetListResponse, ListMetaData} from "@/services/lists";
import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import "primeicons/primeicons.css";
import {TypeHandler} from "@/services/typehandler";
import {BadRequestException, NotFoundException} from "@/services/client";
import {hasText} from "@/services/helpers";
import {createToast, Toaster} from "@/components/Toaster";
import {handleGeneralErrors} from "@/app/ErrorHandler";
import {toHumanReadable} from "@/services/timeutils";

/**
 *
 */
export default function ListPage({params}: { params: { key: string } }) {
    // The key of the current list.
    const listKey: string = params.key;

    // Load message. Displayed during fetch of list details from server.
    const [loadMessage, setLoadMessage] = useState<string | null>("Loading ...");
    const [header, setHeader] = useState<ListMetaData>();
    const [entries, setEntries] = useState<EntryResponse[]>([]);
    const [leftToRight, setLeftToRight] = useState<boolean>(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null)

    //
    // Reference.
    //
    const inputRef: React.RefObject<HTMLInputElement> = useRef<HTMLInputElement>(null);
    const buttonRef: React.RefObject<Button> = useRef<Button>(null);
    const toaster: Toaster = createToast();

    function fetchList(): void {
        getList(listKey, 25, false)
            .then((response: GetListResponse) => {
                setHeader(response.meta);
                setLeftToRight(response.meta.leftToRight);
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

    /**
     * Handles 'Add' operation of new text entry.
     */
    function handleClick() {
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
                    .else(() => handleGeneralErrors(error, toaster));
            })

        inputRef.current.value = "";
        inputRef.current.focus();
    }

    /**
     * Traps 'Enter' key can emulate user click on the 'Add' button.
     *
     * @param source Keyboard event data.
     */
    function handleKey(source: React.KeyboardEvent) {
        if (source.key == "Enter") {
            handleClick();
        }
    }

    let listAgeMessage: string | undefined = undefined;
    if (header) {
        const createdAt: number = Date.parse(header.createdAt);
        const updateAt: number = Date.parse(header.updatedAt);
        const timeZoneOffsetMillis: number = new Date().getTimezoneOffset() * 60 * 1000;
        const now: number = Date.now() + timeZoneOffsetMillis;

        const createdMessage: string = toHumanReadable(now - createdAt);
        const updatedMessage: string = toHumanReadable(now - updateAt)

        if (createdMessage === updatedMessage) {
            listAgeMessage = `Created ${createdMessage} ago.`
        } else {
            listAgeMessage = `Created ${createdMessage} ago, updated ${updatedMessage} ago.`
        }
    }

    return (
        <main className="flex flex-col min-h-screen items-center justify-between p-24">
            <div className="w-full items-center max-w-2xl default-font">
                {loadMessage == null ?
                    <div className="flex flex-row">
                        <div className="py-3 px-3">
                            <div className="pi pi-arrow-circle-left"
                                 onClick={() => document.location.href = "/"}
                                 title="Back to home page">
                            </div>
                        </div>
                        <div className="w-full" dir={leftToRight ? "ltr" : "rtl"}>
                            <div className="flex flex-col w-full py-0 font-secondary-header">
                                {header?.title}
                            </div>
                            <div className="w-full">
                                <div className="flex flex-row">
                                    <div className="flex flex-col">
                                        <InputText id="listName" type="text"
                                                   className="border-solid border-1 h-8 p-0 border-gray-200 rounded-sm w-96"
                                                   onKeyDown={handleKey}
                                                   ref={inputRef}/>

                                        <div className="text-sm text-gray-500 text-justify" dir="ltr">
                                            {listAgeMessage}
                                        </div>
                                    </div>
                                    <div>
                                        <Button className="mx-3 h-8 border-2 border-gray-200" label="Add"
                                                onClick={handleClick}
                                                ref={buttonRef}/>
                                    </div>
                                </div>

                                <div className="py-2 mb-4 text-sm text-red-600 rounded-lg" role="alert">
                                    <div className="text-lg h-5">{errorMessage}</div>
                                </div>

                                <table className="table-auto align-top">
                                    {entries.map((v: EntryResponse) => {
                                        return <tr key={"text-" + v.id}>
                                            <td className="align-top"><span className="pr-3">{v.index}.</span></td>
                                            <td><span>{v.text}</span></td>
                                        </tr>
                                    })}
                                </table>

                            </div>
                        </div>
                    </div>
                    : <span>{loadMessage}</span>
                }
            </div>
            {toaster.element()}
        </main>
    )
}