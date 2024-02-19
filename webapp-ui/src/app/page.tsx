'use client';

import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import React, {ChangeEvent, ReactElement, useState} from "react";
import {createList} from "@/services/lists";
import {BadRequestException} from "@/services/client";
import {TypeHandler} from "@/services/typehandler";
import {hasText} from "@/services/helpers";
import {createToast, Toaster} from "@/components/Toaster";
import {handleGeneralErrors} from "@/app/ErrorHandler";
import {Checkbox} from "primereact/checkbox";

/**
 * State of current message displayed to the user. Can be either an informative message, such as 'Creating list ...'
 * or validation error message (such as 'List name too short').
 */
interface Message {
    message: string,
    error: boolean
}

export default function Home() {
    //
    // Component states.
    //
    const [listName, setListName] = useState("");
    const [rtl, setRtl] = useState(false);
    const [message, setMessage] = useState<Message | null>(null);
    const [buttonDisabled, setButtonDisabled] = useState(false);

    const toaster: Toaster = createToast();

    /**
     * Handles user clicking on 'Create' button.
     *
     * Perform validations and sets error messages as appropriate.
     * If list name is valid, creates a new list by addressing server side.
     */
    function handleSubmitRequest(): void {
        // Trim list name. We don't like spaces.
        let name: string = listName.trim()
        setListName(name)

        if (!hasText(name)) {
            setMessage({message: "Missing list name.", error: true})
        } else {
            setMessage({message: "Creating list ...", error: false})
            setButtonDisabled(true);

            // Try to create the list.
            createList(name, !rtl)
                .then(response => {
                    document.location.href = "/lists/" + response.key;
                })
                .catch((error) => {
                    // Clear current validation or progress message. Enable 'Create' button.
                    setMessage(null);
                    setButtonDisabled(false);

                    TypeHandler.handle(error)
                        .on(BadRequestException, (badRequest: BadRequestException) => {
                            setMessage({message: badRequest.message!, error: true});
                        })
                        .else(() => handleGeneralErrors(error, toaster));
                })
        }
    }

    /**
     * Handle list name input-change -- capture
     * @param event
     */
    function handleInputChange(event: ChangeEvent<HTMLInputElement>): boolean {
        setListName(event.target.value)
        setMessage(null);
        return true;
    }

    //
    // Generate message element based on type of available message (if any).
    //
    let messageEl: ReactElement | undefined;
    if (message != null) {
        if (message.error) {
            messageEl = <div className="py-2 mb-4 text-sm text-red-600 rounded-lg" role="alert">
                <span className="text-lg">{message.message}</span>
            </div>
        } else {
            messageEl = <div className="py-2 mb-4 text-sm text-green-600 rounded-lg">
                <span className="text-lg">{message.message}</span>
            </div>
        }
    } else {
        messageEl = undefined
    }

    return (
        <div>
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
                                       onChange={(e: ChangeEvent<HTMLInputElement>) => handleInputChange(e)}
                            />
                            <Button
                                label="Create"
                                className="h-12 mx-3 border-solid border-2 border-gray-200 disabled:bg-slate-50 disabled:text-slate-500 disabled:border-slate-200 disabled:shadow-none"
                                onClick={() => handleSubmitRequest()}
                                disabled={buttonDisabled}
                            />
                        </div>
                        <div className="my-2">
                            <Checkbox inputId="rightToLeft"
                                      className="my-1"
                                      checked={rtl}
                                      onChange={(event) => setRtl(event.target.checked!)}
                            />
                            <label htmlFor="rightToLeft" className="ml-2">Right-to-left list</label>
                        </div>

                        {messageEl}
                    </div>

                </div>
            </main>

            {toaster.element()}
        </div>
    );
}

