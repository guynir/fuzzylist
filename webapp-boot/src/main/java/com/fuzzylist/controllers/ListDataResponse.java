package com.fuzzylist.controllers;

import java.util.List;

/**
 * Represents a page response for querying page of texts for a given list.
 *
 * @param meta    List metadata, such as title, key and direction.
 * @param entries List of text entries.
 */
public record ListDataResponse(HeaderResponse meta, List<EntryResponse> entries) {
}
