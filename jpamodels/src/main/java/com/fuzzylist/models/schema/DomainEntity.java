package com.fuzzylist.models.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>A JPA entity representing a schema domain. A domain can be type of grouping, such as application name, username
 * or any logical representation of a group.
 * </p>
 * <p>Domains create a namespace for schema uniqueness. In such a way, the same schema name can be reused in different
 * domains.
 * </p>
 * <p>A domain has two core fields: <i>name</i> and <i>unifiedName</i>.
 * </p>
 * The <i>name</i> field the name of the domain as requested by the platform user (e.g.: FuzzyList). The
 * <i>unifiedName</i> is a unified upper-case representation of the domain name. It is used to ensure that
 * domain names uniqueness are case-insensitive.
 *
 * @author Guy Raz Nir
 * @since 2025/04/09
 */
@Entity
@Table(name = "schema_domain")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainEntity {

    /**
     * Domain unique id (PK).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Domain name.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Domain name, in upper-case form.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String unifiedName;
}
