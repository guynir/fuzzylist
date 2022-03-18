package com.fuzzylist.repositories;

import com.fuzzylist.models.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for {@link ListEntity} entity.
 *
 * @author Guy Raz Nir
 * @since 2022/03/15
 */
@Repository
public interface ListEntityRepository extends JpaRepository<ListEntity, Long> {

    /**
     * Find a list by its key.
     *
     * @param listKey List key.
     * @return List entity matching the key.
     */
    @Query("FROM ListEntity WHERE key = :key")
    Optional<ListEntity> findByKey(@Param("key") String listKey);

}
