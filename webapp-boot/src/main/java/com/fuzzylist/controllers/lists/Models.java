package com.fuzzylist.controllers.lists;

import java.time.LocalDateTime;
import java.util.List;

/**
 * All web request/response models for lists controller.
 *
 * @author Guy Raz Nir
 * @since 2024/02/04
 */
public class Models {

    /**
     * Web request to add a text to a list.
     *
     * @param text Text to add.
     * @author Guy Raz Nir
     * @since 2022/03/20
     */
    public record AddTextRequest(String text) {
    }

    /**
     * Web request to create a new list.
     *
     * @param title       List title.
     * @param leftToRight {@code true} for a left-to-right display, {@code false} right-to-left display.
     * @author Guy Raz Nir
     * @since 2022/03/20
     */
    public record CreateListRequest(String title, Boolean leftToRight) {
    }

    /**
     * Represents an HTTP response describing a list.
     *
     * @param key         List key - represents the public identifier of the list.
     * @param title       List title.
     * @param leftToRight {@code true} - if list should be displayed from left-to-right, {@code false} if right-to-left.
     * @param createdAt   Date and time when the list was created.
     * @param updatedAt   Date and time when the list was last updated.
     */
    public record HeaderResponse(String key,
                                 String title,
                                 Boolean leftToRight,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
    }

    /**
     * Represents a page response for querying page of texts for a given list.
     *
     * @param meta    List metadata, such as title, key and direction.
     * @param entries List of text entries.
     */
    public record ListDataResponse(HeaderResponse meta, List<EntryResponse> entries) {
    }

    /**
     * Represents a web response of a single text record.
     *
     * @param id    Internal identifier of the text.
     * @param index Index within the list of this text.
     * @param text  The text.
     */
    public record EntryResponse(long id, int index, String text) {
    }
}
