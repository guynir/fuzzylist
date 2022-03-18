package com.fuzzylist.jpa.tool.cli;

import picocli.CommandLine;

/**
 * Parse command line arguments.
 *
 * @author Guy Raz Nir
 * @since 2021/10/01
 */
public class CommandLineArgumentsParser {

    /**
     * Parse command line arguments and return a {@link DatabasePropertiesCommand} object with all relevant data.
     *
     * @param args Command line arguments.
     * @return Database properties
     * @throws IllegalStateException If case of command-line parsing error.
     */
    public static DatabasePropertiesCommand parse(String[] args) throws IllegalStateException {
        System.out.println("""
                        
                JPA schema management tool V1.0
                ===============================
                """);

        CommandLineHandler props = new CommandLineHandler();
        CommandLine cli = new CommandLine(props);

        CommandLine.ParseResult result;
        try {
            result = new CommandLine(props).parseArgs(args);
        } catch (CommandLine.MissingParameterException ex) {
            System.err.println(ex.getMessage());
            ex.getCommandLine().usage(System.out);
            throw new IllegalStateException();
        }

        // If caller requested help message or did not provide any sub-command, show help message.
        if (result.isUsageHelpRequested() || !result.hasSubcommand()) {
            cli.usage(System.out);
            return null;
        }

        // If caller requested version information.
        if (result.isVersionHelpRequested()) {
            cli.printVersionHelp(System.out);
            return null;
        }

        // If a sub-command specified - try to figure out what to do.
        if (result.hasSubcommand()) {
            CommandLine.ParseResult subcommandResult = result.subcommand();
            CommandLine.Model.CommandSpec cs = subcommandResult.commandSpec();
            if (subcommandResult.isUsageHelpRequested()) {
                cs.commandLine().usage(System.out);
                return null;
            } else {
                if (cs.userObject() instanceof CommandLine.HelpCommand hc) {
                    cli.usage(System.out);
                    return null;
                }
                return (DatabasePropertiesCommand) cs.userObject();
            }
        }

        // For some strange case we reached this location - display error message.
        System.err.println("Unexpected error. Aborting.");
        throw new IllegalStateException();
    }
}
