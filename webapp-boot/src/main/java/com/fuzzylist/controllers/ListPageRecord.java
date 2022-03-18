package com.fuzzylist.controllers;

import java.util.List;

/**
 * Represents a page response for querying page of texts for a given list.
 *
 * @param entries List of text entries.
 */
public record ListPageRecord(List<ListTextRecord> entries) {
}
