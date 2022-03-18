package com.fuzzylist.jpa.tool.cli;

import picocli.CommandLine.Option;

/**
 * Picocli command line options for update/validate commands.
 *
 * @author Guy Raz Nir
 * @since 2021/10/01
 */
public abstract class DatabasePropertiesCommand {

    /**
     * Type of command requested.
     */
    public enum DatabaseCommandEnum {
        VALIDATE, UPDATE
    }

    /**
     * Database hostname or IP address.
     */
    @Option(names = {"-H", "--host"}, required = true, description = "Database host name.")
    public String host;

    /**
     * Optional database port.
     */
    @Option(names = {"-P", "--port"}, description = "Database port name (optional, defaults to '5432').")
    public int port = 5432;

    /**
     * Database user.
     */
    @Option(names = {"-U", "--user"}, required = true, description = "Username.")
    public String username;

    /**
     * Database password.
     */
    @Option(names = {"-p", "--password"}, required = true, description = "Password.")
    public String password;

    /**
     * Optional database schema.
     */
    @Option(names = {"-S", "--schema"}, description = "Schema name (optional, defaults to 'advishag').")
    public String schemaName = "advishag";

    /**
     * Show extended error information (defaults to {@code false}).
     */
    @Option(names = {"-x", "-X"}, description = "Show extended information in case of error.")
    public boolean extendedInformation = false;

    /**
     * Command type to execute.
     */
    public final DatabaseCommandEnum command;

    /**
     * Class constructor.
     *
     * @param command Command to execute.
     */
    protected DatabasePropertiesCommand(DatabaseCommandEnum command) {
        this.command = command;
    }

}
