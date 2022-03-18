package com.fuzzylist.repositories;

import com.fuzzylist.models.ListTextEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Custom Spring JPA repository for complex queries.
 *
 * @author Guy Raz Nir
 * @since 2022/03/17
 */
@Repository
public class ListTextEntityRepositoryImpl implements ListTextEntryRepositoryCustom {

    /**
     * Injected entity manager. Required to execute queries manually.
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ListTextEntity> fetchTextEntries(String listKey, Integer index, int limit, boolean ascending) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT t FROM ListTextEntity t WHERE t.parent.key = :listKey");
        if (index != null) {
            query.append(" AND t.index ");
            query.append((ascending ? ">" : "<"));
            query.append(":index");
        }
        query.append(" ORDER BY t.index ").append((ascending ? "ASC" : "DESC"));

        Query q = entityManager.createQuery(query.toString())
                .setParameter("listKey", listKey)
                .setMaxResults(limit);
        if (index != null) {
            q.setParameter("index", index);
        }

        //noinspection unchecked
        return q.getResultList();
    }
}
