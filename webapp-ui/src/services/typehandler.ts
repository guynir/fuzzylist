/**
 * Type handler allows an easy routing to a specific function based on an object type.
 * For example:
 *
 *      let response = ....
 *
 *      TypeHandler.handle(response)
 *          .onNull(() => { .... })             // Called when our 'response' is null.
 *          .on(BadRequestResponse, (r : BadRequest) => { ... })
 *          .on(InternalServerErrorResponse (r: InternalServerErrorResponse) => { ... })
 *          .else(() => { ... })                // Called if none of the above handlers invoked.
 *
 *
 * @author Guy Raz Nir
 * @since 2024/02/05
 */
export class TypeHandler {

    /**
     * Object to be evaluated.
     */
    private readonly obj: any;

    /**
     * Flag indicating if any handler was called.
     */
    private consumed: boolean = false;

    /**
     * Class constructor.
     *
     * @param obj Object to be evaluated.
     */
    constructor(obj: any) {
        this.obj = obj;
    }

    /**
     * Factory method for creating a new TypeHandler instance. Same as calling 'new TypeHandler(myObj)'.
     *
     * @param obj Object instance to handle.
     */
    public static handle(obj: any): TypeHandler {
        return new TypeHandler(obj);
    }

    /**
     * Condition matching when our object is null.
     *
     * @param callable Callback to invoke if our object is null.
     */
    public onNull<T extends object>(callable: () => void): TypeHandler {
        if (this.obj === null || this.obj === undefined) {
            callable();
            this.consumed = true;
        }
        return this;
    }

    /**
     * Conditional call when out object matches 'matchingType.
     *
     * @param matchingType Type of object to match to.
     * @param callable Callable to invoke if our object matches 'matchingType'.
     */
    public on<T extends object>(matchingType: any, callable: (value: T) => void): TypeHandler {
        if (this.obj && this.obj.constructor.name == matchingType.name) {
            callable(this.obj);
            this.consumed = true;
        }
        return this;
    }

    /**
     * Define a condition that issue a call to 'callable' if neither of the previous conditions met. This
     * condition must always be last.
     *
     * @param callable Callable to invoke if none of the previous were called.
     */
    public else<T extends object>(callable: (value: any) => void): TypeHandler {
        if (this.consumed && !this.consumed) {
            callable(this.obj);
        }
        return this;
    }

}