package com.fuzzylist.services;

import com.fuzzylist.common.id.IdGenerator;
import com.fuzzylist.models.ListEntity;
import com.fuzzylist.models.ListTextEntity;
import com.fuzzylist.repositories.ListEntityRepository;
import com.fuzzylist.repositories.ListTextEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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

    private final ListEntityRepository listEntityRepository;

    private final ListTextEntityRepository listTextEntityRepository;

    /**
     * Class logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ListManagementServiceImpl.class);

    /**
     * Class constructor.
     */
    public ListManagementServiceImpl(@Qualifier("listKeyGenerator") IdGenerator listKeyGenerator,
                                     ListEntityRepository listEntityRepository,
                                     ListTextEntityRepository listTextEntityRepository) {
        this.listKeyGenerator = listKeyGenerator;
        this.listEntityRepository = listEntityRepository;
        this.listTextEntityRepository = listTextEntityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListEntity> fetchLists() {
        return listEntityRepository.findAll();
    }

    @Override
    @Transactional
    public ListEntity createList(String title, boolean leftToRight) throws IllegalArgumentException {
        Assert.notNull(title, "Title cannot be null.");

        assertLength(title, MIN_TEXT_LENGTH, MAX_TITLE_LENGTH, "Title");
        ListEntity newList = new ListEntity(listKeyGenerator.generate(), title, leftToRight);
        listEntityRepository.save(newList);
        logger.info("Created new list: '{}' with key '{}'.", title, newList.key);

        return newList;
    }

    @Override
    @Transactional
    public ListTextEntity addListText(String listKey, String text)
            throws IllegalArgumentException, UnknownListException {
        // No need to test 'listKey' for null. It is done by 'fetchList'.
        Assert.notNull(text, "Text cannot be null.");
        assertLength(text, MIN_TEXT_LENGTH, MAX_TEXT_LENGTH, "Text");

        // Fetch the list. It also makes to to assert the list exists.
        ListEntity list = fetchList(listKey);

        int index = listTextEntityRepository.findMaxListIndex(listKey).orElse(0) + 1;
        ListTextEntity newText = new ListTextEntity(list, index, text);
        listTextEntityRepository.save(newText);

        return newText;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListTextEntity> fetchListTexts(String listKey,
                                               Integer startAfterIndex,
                                               Integer numberOfTexts,
                                               boolean orderIndexAscending) throws IllegalArgumentException {
        // Fetch list just to make sure it exists.
        fetchList(listKey);
        if (numberOfTexts == null) {
            numberOfTexts = ListManagementService.PAGE_SIZE;
        }

        logger.info("Fetching {} text entries for list '{}', order: {}, starting after index {}.",
                numberOfTexts,
                listKey,
                orderIndexAscending ? "ASCENDING" : "DESCENDING",
                startAfterIndex != null ? startAfterIndex : "n/a");
        return listTextEntityRepository.fetchTextEntries(listKey, startAfterIndex, numberOfTexts, orderIndexAscending);
    }

    /**
     * Fetch a list denoted by its public key.
     *
     * @param listKey List key to search by.
     * @return List matching the key.
     * @throws IllegalArgumentException If <i>listKey</i> is {@code null}.
     * @throws UnknownListException     If list does not exist.
     */
    protected ListEntity fetchList(String listKey) throws IllegalArgumentException, UnknownListException {
        Assert.notNull(listKey, "List key cannot be null.");
        return listEntityRepository.findByKey(listKey)
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
