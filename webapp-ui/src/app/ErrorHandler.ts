import {Toaster} from "@/components/Toaster";
import {TypeHandler} from "@/services/typehandler";
import {InternalServerError, ServiceUnavailable} from "@/services/client";

/**
 * Handler global errors, such as service unavailable, server error, timeouts, ....
 *
 * @param error Error to inspect and handle.
 * @param toaster Toaster to use for displaying error messages.
 */
export function handleGeneralErrors(error: any, toaster: Toaster) {
    TypeHandler.handle(error)
        .on(InternalServerError, () => {
            toaster.showErrorMessage("Oops! We had an internal error. Please contact support.");
        })
        .on(ServiceUnavailable, () => {
            toaster.showErrorMessage("Hmm..... service is temporary unavailable. Apologies.");
        })
        .else(() => {
            toaster.showErrorMessage("Unexpected error occurred. Please contact our support.");
        });
}