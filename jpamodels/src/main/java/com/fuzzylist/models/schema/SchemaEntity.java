package com.fuzzylist.models.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * <p>This entity represents a schema metadata. It documents properties such as the domain the schema belongs to,
 * its unique name within the schema, creation timestamp and other properties, such as its public key.
 * </p>
 * <p>
 * The actual definition of a schema and its latest revision and update timestamp are stored managed by its children --
 * the {@code SchemaDefinitionEntity} entity.
 *
 * @author Guy Raz Nir
 * @since 2025/04/09
 */
@Entity
@Table(name = "schema_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemaEntity {

    /**
     * Schema unique id (PK).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Public identifier of schema.
     */
    @Column(name = "object_key", nullable = false, unique = true, length = 50)
    private String key;

    /**
     * Domain which this schema belongs to.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private DomainEntity domain;

    /**
     * Name of schema.
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Timestamp of when this schema was created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * <p>Holds the latest definition revision of this schema.
     * </p>
     * A value of -1 indicates that this schema has no definition set yet.
     */
    @Column(nullable = false)
    private Integer latestRevision;

    /**
     * Class constructor.
     *
     * @param domain Domain this schema belongs to.
     * @param name   Name of this schema.
     */
    public SchemaEntity(DomainEntity domain, String name) {
        this(null, null, domain, name, null, -1);
    }

    /**
     * Class constructor.
     *
     * @param key       Public key of this schema.
     * @param domain    Domain this schema belongs to.
     * @param name      Name of this schema.
     * @param createdAt Creation timestamp of this schema.
     */
    public SchemaEntity(String key, DomainEntity domain, String name, Instant createdAt) {
        this(null, key, domain, name, createdAt, -1);
    }

    /**
     * @return A string representation of the full schema name in the format "domain/schema".
     */
    public String getQualifiedName() {
        return domain.getName() + "/" + name;
    }

    /**
     * Increment the latest revision. Used when adding a new schema definition to this schema.
     */
    public void incrementRevision() {
        latestRevision++;
    }
}
