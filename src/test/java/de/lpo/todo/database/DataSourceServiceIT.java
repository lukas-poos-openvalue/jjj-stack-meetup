package de.lpo.todo.database;

import de.lpo.utils.IntegrationTestDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class DataSourceServiceIT extends IntegrationTestDatabase {
    @BeforeAll
    @Override
    protected void beforeAll() {
        super.beforeAll();
        appCtx.configService.init();
        appCtx.dataSourceService.init();
    }

    @Test
    void should_provide_working_datasource() throws SQLException {
        // GIVEN
        final var datasource = appCtx.dataSourceService.getDataSource();

        // WHEN
        int result = -1;
        try (final var conn = datasource.getConnection()) {
            final var rs = conn.createStatement().executeQuery("select 1");
            if (rs.next()) {
                result = rs.getInt(1);
            }
        }

        // THEN
        assertThat(result).isEqualTo(1);
    }
}