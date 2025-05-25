package com.fuzzylist.repositories.schema;

import com.fuzzylist.models.schema.SchemaTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for {@link SchemaTransactionEntity} entity.
 *
 * @author Guy Raz Nir
 * @since 2025/05/09
 */
@Repository
public interface SchemaTransactionRepository extends JpaRepository<SchemaTransactionEntity, Long> {
}
