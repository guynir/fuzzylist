'use client';

import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import React, {ChangeEvent, ReactElement, useState} from "react";
import {createList} from "@/services/lists";
import {BadRequestException} from "@/services/client";
import {TypeHandler} from "@/services/typehandler";
import {hasText} from "@/services/helpers";

/**
 * Handles user clicking on 'Create' button.
 *
 * Perform validations and sets error messages as appropriate.
 * If list name is valid, creates a new list by addressing server side.
 *
 * @param name Current name of list.
 * @param setName Function for setting list name (requires) for adjusting list name.
 * @param setErrorMessage Function for setting error message.
 * @param setProgressMessage Function for setting progress message.
 * @param setButtonDisabled Function for disabling 'Create' button.
 */
function handleSubmitRequest(name: string,
                             setName: (value: string) => void,
                             setErrorMessage: (value: string | null) => void,
                             setProgressMessage: (value: string) => void,
                             setButtonDisabled: (value: boolean) => void,): void {
    // Trim list name. We don't like spaces.
    name = name.trim()
    setName(name)

    if (!hasText(name)) {
        setErrorMessage("Missing list name.")
    } else {
        setErrorMessage("")
        setProgressMessage("Creating new list ...")
        setButtonDisabled(true);

        // Try to create the list.
        createList(name)
            .then(response => {
                document.location.href = "/lists/" + response.key;
            })
            .catch((error) => {
                let message: string | null = null;
                console.log(error);

                TypeHandler.handle(error)
                    .on(BadRequestException, (badRequest: BadRequestException) => {
                        message = badRequest.message
                    })
                    .else(() => message = "Unexpected error.");

                setErrorMessage(message);
                setProgressMessage("");
                setButtonDisabled(false);
            })
    }
}

export default function Home() {
    //
    // Component states.
    //
    const [listName, setListName] = useState("")
    const [error, setError] = useState<string | null>("")
    const [progressMessage, setProgressMessage] = useState("")
    const [buttonDisabled, setButtonDisabled] = useState(false)

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
        setListName(event.target.value)
        setError("")
        return true;
    }

    //
    // Generate message element based on type of available message (if any).
    //
    let messageEl: ReactElement;
    if (hasText(error)) {
        messageEl = <div className="py-2 mb-4 text-sm text-red-600 rounded-lg" role="alert">
            <span className="text-lg">{error}</span>
        </div>
    } else if (progressMessage.length > 0) {
        messageEl = <div className="py-2 mb-4 text-sm text-green-600 rounded-lg">
            <span className="text-lg">{progressMessage}</span>
        </div>
    } else {
        messageEl = <span></span>
    }

    return (
        <main className="flex flex-col min-h-screen  items-center justify-between p-24">
            <div className="w-full items-center max-w-2xl">
                <div className="w-full text-center font-master-header">FuzzyList</div>
                <div className="w-full text-center font-secondary-header">
                    A place for strange, bizarre, curious, remarkable and funny lists!
                </div>

                <div className="py-10 default-font" style={{fontWeight: 200}}>
                    <div>
                        <label htmlFor="listName">Create a new list:</label>
                    </div>
                    <div className="">
                        <InputText id="listName" type="text"
                                   value={listName}
                                   className="border-solid border-2 h-12 px-4 border-gray-200"
                                   onChange={(e: ChangeEvent<HTMLInputElement>) => handleInputChange(e)}/>
                        <Button
                            label="Create"
                            className="h-12 mx-3 border-solid border-2 border-gray-200 disabled:bg-slate-50 disabled:text-slate-500 disabled:border-slate-200 disabled:shadow-none"
                            onClick={() => handleSubmitRequest(listName, setListName, setError, setProgressMessage, setButtonDisabled)}
                            disabled={buttonDisabled}
                        />

                    </div>

                    {messageEl}
                </div>

            </div>
        </main>
    );
}

