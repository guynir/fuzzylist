package com.fuzzylist.repositories;

import com.fuzzylist.models.ListEntryEntity;

import java.util.List;

public interface ListEntryEntityRepositoryCustom {

    /**
     * Fetch list of text entries. Entries are ordered by {@link ListEntryEntity#index} field.
     *
     * @param listKey   List key.
     * @param index     Optional index to start after.
     * @param limit     Maximum number of entries to return.
     * @param ascending {@code true} if to order entries in ascending order, {@code false} if descending.
     * @return List of entries.
     */
    List<ListEntryEntity> fetchEntries(String listKey, Integer index, int limit, boolean ascending);

}
