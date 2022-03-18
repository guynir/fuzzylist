package com.fuzzylist.controllers;

/**
 * Represents a web response of a single text record.
 *
 * @param id    Internal identifier of the text.
 * @param index Index within the list of this text.
 * @param text  The text.
 */
public record ListTextRecord(long id, int index, String text) {
}
