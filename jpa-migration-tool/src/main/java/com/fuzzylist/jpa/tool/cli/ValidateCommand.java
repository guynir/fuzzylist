package com.fuzzylist.jpa.tool.cli;

import picocli.CommandLine.Command;

/**
 * Picocli subcommand line representing database update..
 *
 * @author Guy Raz Nir
 * @since 2021/10/01
 */
@Command(name = "validate", description = "Validate database schema.", mixinStandardHelpOptions = true)
public class ValidateCommand extends DatabasePropertiesCommand {

    /**
     * Class constructor.
     */
    protected ValidateCommand() {
        super(DatabaseCommandEnum.VALIDATE);
    }
}
