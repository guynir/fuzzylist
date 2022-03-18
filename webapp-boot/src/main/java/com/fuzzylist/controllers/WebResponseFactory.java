package com.fuzzylist.controllers;

import com.fuzzylist.models.ListEntity;
import com.fuzzylist.models.ListTextEntity;

/**
 * Generate web responses from a given internal entities.
 *
 * @author Guy Raz Nir
 * @since 2022/03/18
 */
public class WebResponseFactory {

    public static ListRecord toRecord(ListEntity entity) {
        return new ListRecord(entity.key, entity.title, entity.leftToRight);
    }

    public static ListTextRecord toRecord(ListTextEntity entity) {
        return new ListTextRecord(entity.id, entity.index, entity.text);
    }
}
