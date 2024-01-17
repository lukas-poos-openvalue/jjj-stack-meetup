package de.lpo.todo.database;

import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Service for running DB migrations.
 * <p>
 * Uses {@link Liquibase} for the execution of the updates. The migration scripts can be found in the resources.
 * </p>
 */
public class DbMigrationService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String CHANGELOG_PATH = "db-migrations/master.yml";

    private final DataSourceService datasourceService;

    public DbMigrationService(DataSourceService datasourceService) {
        this.datasourceService = datasourceService;
    }

    /**
     * Executes updates to the database using {@link Liquibase}.
     */
    public void updateDatabase() {
        try (final var jdbcConnection = datasourceService.getDataSource().getConnection()) {
            // Create a database wrapper with the JDBC connection
            final var database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(jdbcConnection));

            // [PoI] Create an update command passing the database and the path to the changelogs
            final var updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
            updateCommand.addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, database);
            updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, CHANGELOG_PATH);

            // Run the update command
            LOG.info("Performing database migration with Liquibase");
            updateCommand.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
