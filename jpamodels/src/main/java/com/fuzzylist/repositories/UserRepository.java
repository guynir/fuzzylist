package com.fuzzylist.repositories;

import com.fuzzylist.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for accessing {@link UserEntity}s.
 *
 * @author Guy Raz Nir
 * @since 2025/04/08
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
