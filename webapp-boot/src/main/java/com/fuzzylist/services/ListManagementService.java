package com.fuzzylist.services;

import com.fuzzylist.models.ListHeaderEntity;
import com.fuzzylist.models.ListEntryEntity;

import java.util.List;

/**
 * Definition of service that manages lists - creation, update, modification.
 *
 * @author Guy Raz Nir
 * @since 2022/03/16
 */
public interface ListManagementService {

    /**
     * Minimum length of list title.
     */
    int MIN_TITLE_LENGTH = 3;

    /**
     * Maximum length of list title.
     */
    int MAX_TITLE_LENGTH = 200;

    /**
     * Minimum length of text.
     */
    int MIN_TEXT_LENGTH = 1;

    /**
     * Maximum length of text.
     */
    int MAX_TEXT_LENGTH = 1000;

    /**
     * Default number of texts per list to pull.
     */
    int PAGE_SIZE = 25;

    /**
     * @return A list of all lists entries.
     */
    List<ListHeaderEntity> fetchLists();

    /**
     * Create a new list.
     *
     * @param title       List title.
     * @param leftToRight {@code true} to mark the list as left-to-right or {@code false} for right-to-left.
     * @return List entity.
     * @throws IllegalArgumentException If either <i>title</i> is {@code null}, or out of
     *                                  {@link #MIN_TITLE_LENGTH minimum} and {@link #MAX_TITLE_LENGTH maximum} range.
     */
    ListHeaderEntity createList(String title, boolean leftToRight) throws IllegalArgumentException;

    /**
     * Add a new text to a list.
     *
     * @param listKey List key.
     * @param text    Text to add.
     * @return Text entity created.
     * @throws IllegalArgumentException If either <i>listKey</i> is {@code null}, <i>text</i> is {@code null} or if
     *                                  <i>text</i> length is out of {@link #MIN_TEXT_LENGTH minimum} -
     *                                  {@link #MAX_TEXT_LENGTH maximum} range.
     */
    ListEntryEntity addListText(String listKey, String text) throws IllegalArgumentException, UnknownListException;

    /**
     * Fetch list texts.
     *
     * @param listKey             List key.
     * @param startAfterIndex     Optional index to start after. May be {@code null} to start from first/last text entry.
     * @param numberOfTexts       Optional number of texts to fetch. If {@code null}, {@link #PAGE_SIZE} is used.
     * @param orderIndexAscending {@code true} to order list in ascending order (based on the index field),
     *                            {@code false} if descending order.
     * @return List metadata and text entries.
     * @throws IllegalArgumentException If <i>listKey</i> is {@code null}, <i>startIndex</i> is negative or
     *                                  <i>numberOfTexts</i> zero or negative.
     */
    ListEntries fetchListTexts(String listKey,
                               Integer startAfterIndex,
                               Integer numberOfTexts,
                               boolean orderIndexAscending) throws IllegalArgumentException;
}
