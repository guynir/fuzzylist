package com.fuzzylist.jpa.tool;

import com.fuzzylist.jpa.tool.cli.CommandLineArgumentsParser;
import com.fuzzylist.jpa.tool.cli.DatabasePropertiesCommand;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring boot application that runs Hibernate validation or migration.
 *
 * @author Guy Raz Nir
 * @since 2021/09/30
 */
@SpringBootApplication
@EntityScan(basePackages = "com.fuzzylist.models")
@EnableTransactionManagement
public class SchemaToolApp {

    /**
     * Application's entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        DatabasePropertiesCommand command;
        try {
            command = CommandLineArgumentsParser.parse(args);
            if (command == null) {
                System.exit(0);

                // Not really required. Only here to address compilation issues.
                return;
            }
        } catch (IllegalStateException ex) {
            System.exit(1);

            // Not really required. Only here to address compilation issues.
            return;
        }

        //
        // Based on the command type, populate configuration and messages.
        //
        String ddlMode;
        String announcementMessage;
        String successMessage;
        if (command.command.equals(DatabasePropertiesCommand.DatabaseCommandEnum.VALIDATE)) {
            ddlMode = "validate";
            announcementMessage = "Performing database validation...";
            successMessage = "Database is OK !";
        } else if (command.command.equals(DatabasePropertiesCommand.DatabaseCommandEnum.UPDATE)) {
            ddlMode = "update";
            announcementMessage = "Performing database schema update...";
            successMessage = "Database is up to date !";
        } else {
            return;
        }

        String jdbcURL = String.format("jdbc:postgres:%s:%s/%s", command.host, command.port, command.schemaName);
        System.setProperty("spring.jpa.hibernate.ddl-auto", ddlMode);
        System.setProperty("spring.datasource.hikari.url", jdbcURL);
        System.setProperty("spring.datasource.hikari.username", command.username);
        System.setProperty("spring.datasource.hikari.password", command.password);

        try {
            System.out.println(announcementMessage);
            SpringApplication.run(SchemaToolApp.class, args);
            System.out.println(successMessage);
        } catch (Exception ex) {
            // If we can extract a root cause with meaning (e.g.: schema error exception) -- display human-readable
            // message.
            Throwable rootCause = getRootCause(ex);
            if (!command.extendedInformation) {
                System.err.println("ERROR: " + rootCause.getMessage());
            } else {
                // If we could not figure out the exact error, dump stacktrace to console.
                ex.printStackTrace();
            }
        }
    }

    /**
     * Extract root cause of a given exception.
     *
     * @param t Exception to extract cause from.
     * @return Root cause.
     */
    private static Throwable getRootCause(Throwable t) {
        if (t.getCause() != null) {
            return getRootCause(t.getCause());
        } else {
            return t;
        }
    }
}
