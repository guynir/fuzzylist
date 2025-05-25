package com.fuzzylist.models.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * <p>This entity maintains a schema definition. Each such entity maintains a revision of a schema, the actual
 * definition and a reference to the schema it belongs to.
 * </p>
 * Each revision maintains a list of definition-operations that define the changes made to the schema definition
 * since its last revision.
 *
 * @author Guy Raz Nir
 * @since 2025/05/16
 */
@Entity
@Table(name = "schema_definition",
        uniqueConstraints = @UniqueConstraint(
                name = "schema_definition_unique_key_constraints",
                columnNames = {"parent_id", "revision"}
        ))
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SchemaDefinitionEntity {

    /**
     * Unique identifier for the schema definition.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Timestamp when this definition was created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Revision number of this definition.
     */
    @Column(nullable = false, updatable = false)
    private Integer revision;

    /**
     * Parent schema this definition belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false, updatable = false)
    private SchemaEntity parent;

    /**
     * The schema definition.
     */
    @Lob
    @Column(nullable = false, updatable = false)
    private String definition;

}
