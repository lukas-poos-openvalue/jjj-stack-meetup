package de.lpo.todo.database;

import de.lpo.todo.config.ConfigService;
import org.jooq.*;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.*;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * Provides easy access the database using the {@link org.jooq.DSLContext} from jOOQ.
 */
public class JooqService {
    private final ConfigService configService;
    private final DataSourceService datasourceService;

    private DSLContext dsl;

    public JooqService(ConfigService configService, DataSourceService datasourceService) {
        this.configService = configService;
        this.datasourceService = datasourceService;
    }

    /**
     * @return the lazy-initialized instance of the {@link DSLContext}.
     */
    public DSLContext create() {
        if (dsl == null) {
            dsl = initializeDslContext();
        }
        return dsl;
    }

    private DSLContext initializeDslContext() {
        final var dataSource = datasourceService.getDataSource();
        final var sqlDialect = JDBCUtils.dialect(configService.getConfig().databaseUrl());
        final var settings = new Settings().withRenderNameCase(RenderNameCase.LOWER); // [PoI] Conflict H2 for generation & PostgreSQL on runtime
        return DSL.using(dataSource, sqlDialect, settings);
    }
}
