package com.fuzzylist.services.schema.model.migration;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A collection of migration steps that alters a given schema. Each step may add a column, remove a column, or change
 * an existing column.
 *
 * @author Guy Raz Nir
 * @since 2025/05/19
 */
public class Migrations implements Iterable<Migration> {

    /**
     * The list of migration steps.
     */
    @Getter
    private final List<Migration> migrations = new LinkedList<>();

    /**
     * Class constructor.
     *
     * @param migrations Initial list of migrations to assign to this instance.
     */
    public Migrations(Migration... migrations) {
        for (Migration migration : migrations) {
            addMigration(migration);
        }
    }

    /**
     * Add a new migration step to this collection.
     *
     * @param migration The migration step to be added.
     * @return This instance.
     * @throws IllegalArgumentException     If the given migration is {@code null}.
     * @throws MigrationValidationException If the given migration contains a non-valid definition.
     */
    public Migrations addMigration(Migration migration) throws IllegalArgumentException, MigrationValidationException {
        Assert.notNull(migration, "Migration cannot be null.");

        migration.validate();
        migrations.add(migration);
        return this;
    }

    /**
     * Provide indication if this migrations' container has any migration steps in it.
     *
     * @return {@code true} if this container has any migration steps, {@code false} otherwise.
     */
    public boolean hasMigrations() {
        return !migrations.isEmpty();
    }

    @Override
    @NotNull
    public Iterator<Migration> iterator() {
        return migrations.iterator();
    }
}
