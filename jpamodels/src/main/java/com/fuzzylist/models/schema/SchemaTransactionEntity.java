package com.fuzzylist.models.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "schema_transaction")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SchemaTransactionEntity {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the schema this transaction relates to.
     */
    @OneToOne(optional = false)
    @JoinColumn(unique = true, nullable = false, updatable = false)
    private SchemaEntity schema;

    /**
     * Timestamp of when this transaction was created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;
}
