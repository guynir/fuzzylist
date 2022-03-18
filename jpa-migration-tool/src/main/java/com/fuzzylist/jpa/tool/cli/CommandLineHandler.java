package com.fuzzylist.jpa.tool.cli;

import picocli.CommandLine;

/**
 * Picocli command line configuration.
 *
 * @author Guy Raz Nir
 * @since 2021/10/01
 */
@CommandLine.Command(name = "SchemaToolApp",
        subcommands = {CommandLine.HelpCommand.class, ValidateCommand.class, UpdateCommand.class},
        helpCommand = true,
        version = "1.0"
)
public class CommandLineHandler {

    @CommandLine.Option(names = "-x")
    public boolean verbose;
}
