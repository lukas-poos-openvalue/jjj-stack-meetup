package de.lpo.todo.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.lpo.todo.config.ConfigService;
import de.lpo.todo.dependencies.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.invoke.MethodHandles;

/**
 * The service provides pooled connections to the database with a {@link javax.sql.DataSource}.
 */
public class DataSourceService implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ConfigService configService;
    private DataSource dataSource;

    public DataSourceService(ConfigService configService) {
        this.configService = configService;
    }


    /**
     * @return The {@link DataSource} that provides pooled connections to the database.
     * @throws IllegalStateException when the service hasn't been initialized yet
     */
    public DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized yet! Call DataSourceService#init before!");
        }
        return dataSource;
    }

    /**
     * Initializes the {@link DataSource}.
     * <p>
     * Creates a {@link HikariDataSource} with the configured database info and stores it the serivce's state.
     * </p>
     */
    @Override
    public void init() {
        if (dataSource != null) {
            LOG.warn("DataSourceService was initialized before! Skipping init...");
            return;
        }

        // Get the application config (for database info)
        final var config = configService.getConfig();

        // [PoI] Configure a HikariDataSource
        final var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.databaseUrl());
        hikariConfig.setUsername(config.databaseUser());
        hikariConfig.setPassword(config.databasePassword());

        // Create and save the DataSource
        this.dataSource = new HikariDataSource(hikariConfig);
    }
}
