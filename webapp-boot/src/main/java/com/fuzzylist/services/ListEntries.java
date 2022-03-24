package com.fuzzylist.services;

import com.fuzzylist.models.ListHeaderEntity;
import com.fuzzylist.models.ListEntryEntity;

import java.util.List;

/**
 * Data transfer object holding list metadata ({@link ListHeaderEntity} and texts (list of {@link ListEntryEntity}s).
 *
 * @param listHeaderEntity List header.
 * @param entries    Texts of the list.
 * @author Guy Raz Nir
 * @since 2022/03/23
 */
public record ListEntries(ListHeaderEntity listHeaderEntity, List<ListEntryEntity> entries) {
}
