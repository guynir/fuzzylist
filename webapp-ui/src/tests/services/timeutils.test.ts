import {describe, expect, test} from '@jest/globals';
import {exportedForTesting} from "@/services/timeutils";

const {toUnit, TIME_UNITS} = exportedForTesting;

//
// Test 'timeutils' module.
//
describe("timeutils", () => {

    /**
     * Test should successfully convert millis into round number of values based on
     * given unites (i.e.: seconds, minutes, hours, ....).
     */
    test("Should convert milliseconds to seconds", () => {
        const SECOND = 1000;
        const MINUTE = 60 * SECOND;
        const HOUR = 60 * MINUTE;
        const DAY = 24 * HOUR;
        const WEEK = 7 * DAY;
        const YEAR = 365 * DAY;

        expect(toUnit(1.2 * SECOND, TIME_UNITS.SECONDS)).toBe(1);
        expect(toUnit(61 * MINUTE, TIME_UNITS.MINUTES)).toBe(61);
        expect(toUnit(2 * HOUR, TIME_UNITS.HOURS)).toBe(2);
        expect(toUnit(2.5 * DAY, TIME_UNITS.DAYS)).toBe(2);
        expect(toUnit(3.5 * WEEK, TIME_UNITS.WEEKS)).toBe(3);
        expect(toUnit(2 * YEAR, TIME_UNITS.YEARS)).toBe(2);
    })

})