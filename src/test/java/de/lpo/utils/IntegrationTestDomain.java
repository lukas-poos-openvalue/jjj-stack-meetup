package de.lpo.utils;

import org.jooq.generated.Tables;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

/**
 * The base class for integration tests. Performs the following tasks for you:
 * <ul>
 *     <li>Setup test instance per class, not per method</li>
 *     <li>Before all: Check if DB container is running / initialize application context</li>
 *     <li>Before each: Clear the tables</li>
 *     <li>After each: Clear the tables</li>
 * </ul>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTestDomain extends IntegrationTestDatabase {

    /** A list of tables that are expected to be present after application context setup */
    public static final List<String> DOMAIN_TABLES = List.of(
            Tables.TBL_TODO.getName().toLowerCase(),
            Tables.TBL_BOARD_MEMBER.getName().toLowerCase(),
            Tables.TBL_BOARD.getName().toLowerCase(),
            Tables.TBL_USER.getName().toLowerCase()
    );

    /**
     * Check that the database container is running and the application context is initialized.
     */
    @BeforeAll
    @Override
    protected void beforeAll() {
        super.beforeAll();
        appCtx.init();
    }

    /**
     * Clear the domain tables
     */
    @BeforeEach
    protected void beforeEach() {
        clearDomainTables();
    }

    /**
     * Clear the domain tables
     */
    @AfterEach
    protected void afterEach() {
        clearDomainTables();
    }
}
