package com.fuzzylist.jpa.tool.cli;

import picocli.CommandLine.Command;

/**
 * Picocli subcommand line representing database update..
 *
 * @author Guy Raz Nir
 * @since 2021/10/01
 */
@Command(name = "update", description = "Update database schema to match JPA entities.", mixinStandardHelpOptions = true)
public class UpdateCommand extends DatabasePropertiesCommand {

    /**
     * Class constructor.
     */
    protected UpdateCommand() {
        super(DatabaseCommandEnum.UPDATE);
    }

}
