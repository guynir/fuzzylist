package com.fuzzylist.repositories;

import com.fuzzylist.models.ListTextEntity;

import java.util.List;

public interface ListTextEntryRepositoryCustom {

    /**
     * Fetch list of text entries. Entries are ordered by {@link ListTextEntity#index} field.
     *
     * @param listKey   List key.
     * @param index     Optional index to start after.
     * @param limit     Maximum number of entries to return.
     * @param ascending {@code true} if to order entries in ascending order, {@code false} if descending.
     * @return List of entries.
     */
    List<ListTextEntity> fetchTextEntries(String listKey, Integer index, int limit, boolean ascending);

}
