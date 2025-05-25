package com.fuzzylist.repositories.schema;

import com.fuzzylist.models.schema.SchemaDefinitionEntity;
import com.fuzzylist.models.schema.SchemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for accessing {@link SchemaDefinitionEntity}.
 *
 * @author Guy Raz Nir
 * @since 2025/05/18
 */
@Repository
public interface SchemaDefinitionRepository extends JpaRepository<SchemaDefinitionEntity, Long> {

    /**
     * Fetch the latest schema definition for a given schema.
     *
     * @param parent Parent schema to look by.
     * @return Latest schema definition for the given schema or {@code null} if no definition found for schema.
     */
    @Query("SELECT s FROM SchemaDefinitionEntity s WHERE s.parent = :parent AND s.revision = :revision")
    SchemaDefinitionEntity findBySchemaAndRevision(SchemaEntity parent, int revision);

    /**
     * Fetch all schema definitions for a given schema.
     *
     * @param parent Parent schema to look by.
     * @return List of schema definitions for the given schema or an empty list if no definitions found for schema.
     */
    @Query("FROM SchemaDefinitionEntity s WHERE s.parent = :parent")
    List<SchemaDefinitionEntity> findByParent(SchemaEntity parent);
}
