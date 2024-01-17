package de.lpo.utils;

import de.lpo.todo.config.model.Config;
import org.testcontainers.containers.PostgreSQLContainer;

public class ItPostgreSQLContainer extends PostgreSQLContainer<ItPostgreSQLContainer> {
    private static final String IMAGE_VERSION = "postgres:16-alpine";

    @SuppressWarnings("resource")
    public static ItPostgreSQLContainer createInstance() {
        return new ItPostgreSQLContainer()
                .withDatabaseName("test-db")
                .withUsername("test-user")
                .withPassword("test-pw")
                .withExposedPorts(5432)
                .withReuse(true);
    }

    private ItPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    @Override
    public void start() {
        throw new IllegalStateException("Please use #startAndStoreConfig instead!");
    }

    public void startAndStoreConfig() {
        // Start the container
        super.start();

        // Set system properties, so that the dynamic properties appear in the config
        final var sysProps = System.getProperties();
        sysProps.setProperty(Config.KEY_DATABASE_HOST, getHost());
        sysProps.setProperty(Config.KEY_DATABASE_PORT, String.valueOf(getMappedPort(5432)));
        sysProps.setProperty(Config.KEY_DATABASE_NAME, getDatabaseName());
        sysProps.setProperty(Config.KEY_DATABASE_USER, getUsername());
        sysProps.setProperty(Config.KEY_DATABASE_PASSWORD, getPassword());
    }
}
