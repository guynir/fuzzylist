package com.fuzzylist.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user in the system.
 *
 * @author Guy Raz Nir
 * @since 2025/04/08
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /**
     * Unique identifier (PK).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /**
     * E-mail address. Also serves as username (identifies the user publicly).
     */
    @Column(unique = true, nullable = false, length = 320)
    public String email;

    /**
     * Credentials.
     */
    @Column(nullable = false, length = 80)
    public String password;

    /**
     * Optional first-name.
     */
    @Column(nullable = false, length = 100)
    public String firstName;

    /**
     * Optional middle-name.
     */
    @Column(nullable = false, length = 100)
    public String middleName;

    /**
     * Optional last-name.
     */
    @Column(nullable = false, length = 100)
    public String lastName;

    /**
     * Indicates if the user is enabled.
     */
    @Column(nullable = false)
    public boolean enabled;

    /**
     * Indicates if the account is expired. An expired account may be one that was not used for a long time.
     */
    @Column(nullable = false)
    public boolean expired;

    /**
     * Indicates if the account is locked. A locked account may be one that had too many failed login retries or
     * an account accessed from a suspicious location.
     */
    @Column(nullable = false)
    public boolean locked;

    /**
     * Indicates if the credentials have expired.
     */
    @Column(nullable = false)
    public boolean credentialsExpired;


}
