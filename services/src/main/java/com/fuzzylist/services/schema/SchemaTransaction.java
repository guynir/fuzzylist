package com.fuzzylist.services.schema;

import com.fuzzylist.models.schema.SchemaEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * <p>This entity represents a transaction summary status per schema.
 * </p>
 * It acts, in essence, as a readers-write lock. Multiple readers can request lock concurrently. However, when
 * a writer requests a lock, it gets an exclusive lock, prioritizing any other readers.
 *
 * @author Guy Raz Nir
 * @since 2025/05/08
 */
public class SchemaTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(optional = false)
    private SchemaEntity schema;

    private int activeReaders;

    private int activeWriters;

}
