/**
 * Helper to decide if 'obj' is defined (contains data), i.e.: is neither 'null' nor undefined.
 *
 * @param obj Object to evaluate.
 */
export function isDefined(obj: any): boolean {
    return obj !== null && obj !== undefined && obj;
}

/**
 * Test if a given string is defined (non-null and non-undefined) and has any characters which
 * are non-white spaces.
 *
 * @param st String to evaluate.
 */
export function hasText(st: string | null | undefined): boolean {
    return isDefined(st) && st!.trim().length > 0;
}