package de.lpo.todo.database;

import de.lpo.utils.IntegrationTestDatabase;
import de.lpo.utils.IntegrationTestDomain;
import org.jooq.Table;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DbMigrationServiceIT extends IntegrationTestDatabase {
    @BeforeAll
    @Override
    protected void beforeAll() {
        super.beforeAll();
        dropAllTables();
        appCtx.configService.init();
        appCtx.dataSourceService.init();
    }

    @AfterEach
    void afterEach() {
        dropAllTables();
    }

    @Test
    void should_perform_database_migration() {
        // WHEN
        appCtx.dbMigrationService.updateDatabase();

        // THEN
        final var resultTables = appCtx.jooqService.create().meta().getTables();
        final var resultTableNames = getTableNames(resultTables);
        final var expectedTableNames = IntegrationTestDomain.DOMAIN_TABLES;

        assertThat(resultTableNames).containsAll(expectedTableNames);
    }

    private List<String> getTableNames(List<Table<?>> tables) {
        return tables.stream()
                .map(Table::getName)
                .map(String::toLowerCase)
                .toList();
    }
}