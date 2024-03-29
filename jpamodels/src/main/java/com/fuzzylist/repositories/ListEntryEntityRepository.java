package com.fuzzylist.repositories;

import com.fuzzylist.models.ListEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring JPA for accessing {@link ListEntryEntity}.
 *
 * @author Guy Raz Nir
 * @since 2022/03/16
 */
@Repository
public interface ListEntryEntityRepository extends JpaRepository<ListEntryEntity, Long>, ListEntryEntityRepositoryCustom {

    /**
     * Find the highest 'index' value for a given list.
     *
     * @param listKey List key.
     * @return Highest index for a list, or {@link Optional#empty()} if list does not exist or is empty.
     */
    @Query("SELECT MAX(t.index) FROM ListEntryEntity t WHERE parent.key = :key")
    Optional<Integer> findMaxListIndex(@Param("key") String listKey);

}
