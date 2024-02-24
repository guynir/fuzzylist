/**
 * Convert a date/time object to human-readable time interval as follows:
 *      - If time is less than one hour - return "less than an hour".
 *      - If time is less than 24 hours - return "NNN hours".
 *      - If time is less than 30 days - return "DDD days".
 *      - If time is less than 60 days - return "WWW weeks ago".
 *      - If time is up to 24 months - return "MMM month ago".
 *      - If time is more than 24 months - return "YYY years ago".
 *
 * @param millis Number of milliseconds.
 * @return Human readable string.
 */
export function toHumanReadable(millis: number): string {
    // Contains number of hours/days/weeks/years.
    let count: number;

    // Contains unit name -- hour/day/week/year.
    let unitName: string;

    // If timeframe is up to 24 hours ...
    const minutes : number = toUnit(millis, TIME_UNITS.MINUTES);
    const hours: number = toUnit(millis, TIME_UNITS.HOURS);
    let weeks: number = toUnit(millis, TIME_UNITS.WEEKS);
    let days: number = toUnit(millis, TIME_UNITS.DAYS);
    let years: number = toUnit(millis, TIME_UNITS.YEARS);

    if (minutes < 15) {
        return "few minutes"
    }

    if (hours == 0) {
        return "less than an hour";
    }

    if (hours < 24) {
        count = hours;
        unitName = "hour";
    } else if (days < 30) {
        count = days;
        unitName = "day";
    } else if (days < 60) {
        count = weeks;
        unitName = "week";
    } else if (years <= 2) {
        count = Math.round(days / 30);
        unitName = "month";
    } else {
        count = years;
        unitName = "year"
    }

    return `${count} ${unitName}${count > 1 ? "s" : ""}`;
}

enum TIME_UNITS {
    MILLIS = 1,
    SECONDS = 1000,
    MINUTES = 60 * SECONDS,
    HOURS = 60 * MINUTES,
    DAYS = HOURS * 24,
    WEEKS = 7 * DAYS,
    YEARS = 365 * DAYS
}

function toUnit(millis: number, unit: TIME_UNITS): number {
    return Math.floor(millis / unit.valueOf());
}

export const exportedForTesting = {
    TIME_UNITS, toUnit
}