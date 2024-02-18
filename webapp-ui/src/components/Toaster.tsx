import React, {createRef, ReactElement, ReactNode, RefObject, useRef} from "react";
import {Toast} from "primereact/toast";
import {capitalize, isDefined} from "@/services/helpers";

export enum Severity {
    SUCCESS = "success",
    INFO = "info",
    WARNING = "warn",
    ERROR = "error"
}

/**
 * A Toaster is a simple wrapper around 'Toast' component. It encapsulates both the ReactElement itself and set of
 * accessors for controlling the appearance of a toast UI element.
 */
export class Toaster {

    /** The ReactElement itself. */
    private readonly el: ReactElement<Toast>;

    /** The toast object. Required for showing/hiding messages. */
    private readonly toast: Toast;

    /**
     * Class constructor.
     * @param el The element itself.
     * @param ref Reference to the element.
     */
    constructor(el: ReactElement<Toast>, ref: React.RefObject<Toast>) {
        this.el = el
        this.toast = ref.current!;
    }

    /**
     * @return The rendered element.
     */
    public element(): ReactNode {
        return this.el;
    }

    /**
     * Show error message.
     *
     * @param message Message to display.
     */
    public showErrorMessage(message: string) {
        this.showMessage(message, Severity.ERROR);
    }

    /**
     * Show a new message within a toast component.
     *
     * @param message Message to display.
     * @param severity Severity.
     * @param title Optional title. If missing, derived from 'severity'.
     */
    public showMessage(message: string,
                       severity: Severity,
                       title: string | null | undefined = null) {
        if (!isDefined(title)) {
            title = capitalize(severity);
        }

        // Remove any previous  messages.
        this.toast.clear();

        // Show a new message.
        this.toast.show({
            severity: severity,
            summary: title,
            detail: message,
        });
    }
}

/**
 * Creates a new Toast element and a function to show error message inside the Toast element.
 *
 * @return React element representing the Toast and a function to show messages.
 */
export function createToast(): Toaster {
    const ref: RefObject<Toast> = createRef<Toast>();
    const el: ReactElement<Toast> = <Toast
        ref={ref} position="top-left"
        pt={{root: {className: "w-100"}}}
        ptOptions={{mergeSections: true, mergeProps: false}}
    />

    return new Toaster(el, ref);
}