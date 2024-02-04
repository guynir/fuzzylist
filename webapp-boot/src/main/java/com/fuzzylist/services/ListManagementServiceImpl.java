package com.fuzzylist.services;

import com.fuzzylist.common.id.IdGenerator;
import com.fuzzylist.models.ListHeaderEntity;
import com.fuzzylist.models.ListEntryEntity;
import com.fuzzylist.repositories.ListHeaderEntityRepository;
import com.fuzzylist.repositories.ListEntryEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of list management service using JPA repositories.
 *
 * @author Guy Raz Nir
 * @since 2022/03/16
 */
@Service
public class ListManagementServiceImpl implements ListManagementService {

    private final IdGenerator listKeyGenerator;

    private final ListHeaderEntityRepository listHeaderEntityRepository;

    private final ListEntryEntityRepository listEntryEntityRepository;

    /**
     * Class logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ListManagementServiceImpl.class);

    /**
     * Class constructor.
     */
    public ListManagementServiceImpl(@Qualifier("listKeyGenerator") IdGenerator listKeyGenerator,
                                     ListHeaderEntityRepository listHeaderEntityRepository,
                                     ListEntryEntityRepository listEntryEntityRepository) {
        this.listKeyGenerator = listKeyGenerator;
        this.listHeaderEntityRepository = listHeaderEntityRepository;
        this.listEntryEntityRepository = listEntryEntityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListHeaderEntity> fetchLists() {
        return listHeaderEntityRepository.findAll();
    }

    @Override
    @Transactional
    public ListHeaderEntity createList(String title, boolean leftToRight) throws IllegalArgumentException {
        Assert.notNull(title, "Title cannot be null.");

        assertLength(title, MIN_TITLE_LENGTH, MAX_TITLE_LENGTH, "Title");
        ListHeaderEntity newList = new ListHeaderEntity(listKeyGenerator.generate(), title, leftToRight);
        listHeaderEntityRepository.save(newList);
        logger.info("Created a new list: '{}' with key '{}'.", title, newList.key);

        return newList;
    }

    @Override
    @Transactional
    public ListEntryEntity addListText(String listKey, String text)
            throws IllegalArgumentException, UnknownListException {
        // No need to test 'listKey' for null. It is done by 'fetchList'.
        Assert.notNull(text, "Text cannot be null.");
        assertLength(text, MIN_TEXT_LENGTH, MAX_TEXT_LENGTH, "Text");

        // Fetch the list. It also makes to assert the list exists.
        ListHeaderEntity list = fetchList(listKey);

        int index = listEntryEntityRepository.findMaxListIndex(listKey).orElse(0) + 1;
        ListEntryEntity newText = new ListEntryEntity(list, index, text);
        listEntryEntityRepository.save(newText);

        logger.info("Added text (size: {}) to list '{}'.", text.length(), listKey);

        return newText;
    }

    @Override
    @Transactional(readOnly = true)
    public ListEntries fetchListTexts(String listKey,
                                      Integer startAfterIndex,
                                      Integer numberOfTexts,
                                      boolean orderIndexAscending) throws IllegalArgumentException {
        // Fetch list just to make sure it exists.
        ListHeaderEntity listHeaderEntity = fetchList(listKey);
        if (numberOfTexts == null) {
            numberOfTexts = ListManagementService.PAGE_SIZE;
        }

        logger.info("Fetching {} text entries for list '{}', order: {}, starting after index {}.",
                numberOfTexts,
                listKey,
                orderIndexAscending ? "ASCENDING" : "DESCENDING",
                startAfterIndex != null ? startAfterIndex : "n/a");
        List<ListEntryEntity> entries = listEntryEntityRepository.fetchEntries(listKey, startAfterIndex, numberOfTexts, orderIndexAscending);
        return new ListEntries(listHeaderEntity, new ArrayList<>(entries));
    }

    /**
     * Fetch a list denoted by its public key.
     *
     * @param listKey List key to search by.
     * @return List matching the key.
     * @throws IllegalArgumentException If <i>listKey</i> is {@code null}.
     * @throws UnknownListException     If list does not exist.
     */
    protected ListHeaderEntity fetchList(String listKey) throws IllegalArgumentException, UnknownListException {
        Assert.notNull(listKey, "List key cannot be null.");
        return listHeaderEntityRepository.findByKey(listKey)
                .orElseThrow(() -> new UnknownListException("Unknown list: " + listKey));
    }

    /**
     * Assert that a length of a given string is within a given range. If assertion fails,
     * {@code IllegalArgumentException} if thrown.
     *
     * @param st         String length to assert.
     * @param minLength  Minimum string length, inclusive.
     * @param maxLength  Maximum string length, inclusive.
     * @param stringName Name of string, required for error message.
     * @throws IllegalArgumentException If <i>st</i> is out of range.
     */
    private void assertLength(String st, int minLength, int maxLength, String stringName) throws IllegalArgumentException {
        int length = st.length();
        if (length < minLength || length > maxLength) {
            throw new IllegalArgumentException(
                    String.format("%s length is out of bounds (must be in a range of %d and %d; actual length: %d).",
                            stringName,
                            minLength,
                            maxLength,
                            length));
        }
    }
}
