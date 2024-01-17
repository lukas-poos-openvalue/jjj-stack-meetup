package de.lpo.utils;

import de.lpo.todo.dependencies.ApplicationContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTestDatabase {
    /**
     * The PostgreSQL container for out integration tests.
     */
    protected static final ItPostgreSQLContainer CONTAINER = ItPostgreSQLContainer.createInstance();

    /** The application context (unique per test class) */
    protected final ApplicationContext appCtx = new ApplicationContext();

    /**
     * Check that the database container is running
     */
    @BeforeAll
    protected void beforeAll() {
        checkDatabaseRunning();
    }

    protected void checkDatabaseRunning() {
        if (!CONTAINER.isRunning()) {
            CONTAINER.startAndStoreConfig();
        }
    }

    protected void clearDomainTables() {
        try (final var conn = connectToContainer();
             final var stmt = conn.createStatement()) {
            for (final var table : IntegrationTestDomain.DOMAIN_TABLES) {
                stmt.execute("delete from %s where 1=1".formatted(table));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void dropAllTables() {
        try (final var conn = connectToContainer();
             final var stmt = conn.createStatement()) {
            for (final var table : IntegrationTestDomain.DOMAIN_TABLES) {
                stmt.execute("drop table if exists %s cascade".formatted(table));
            }
            stmt.execute("drop table if exists DATABASECHANGELOG cascade");
            stmt.execute("drop table if exists DATABASECHANGELOGLOCK cascade");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection connectToContainer() throws SQLException {
        return CONTAINER.createConnection("");
    }
}
