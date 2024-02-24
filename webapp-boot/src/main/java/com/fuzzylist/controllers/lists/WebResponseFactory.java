package com.fuzzylist.controllers.lists;

import com.fuzzylist.models.ListHeaderEntity;
import com.fuzzylist.models.ListEntryEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generate web responses from a given internal entities.
 *
 * @author Guy Raz Nir
 * @since 2022/03/18
 */
public class WebResponseFactory {

    public static Models.HeaderResponse toRecord(ListHeaderEntity entity) {
        return new Models.HeaderResponse(entity.key, entity.title, entity.leftToRight, entity.createdAt, entity.updatedAt);
    }

    public static Models.EntryResponse toRecord(ListEntryEntity entity) {
        return new Models.EntryResponse(entity.id, entity.index, entity.text);
    }

    public static List<Models.EntryResponse> toRecord(Collection<ListEntryEntity> entities) {
        return entities.stream().map(WebResponseFactory::toRecord).collect(Collectors.toList());
    }
}
