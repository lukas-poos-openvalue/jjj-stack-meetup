package de.lpo.todo.database;

import de.lpo.utils.IntegrationTestDatabase;
import org.assertj.core.groups.Tuple;
import org.jooq.Record2;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.*;

class JooqServiceIT extends IntegrationTestDatabase {
    @BeforeAll
    @Override
    protected void beforeAll() {
        super.beforeAll();
        appCtx.configService.init();
        appCtx.dataSourceService.init();
    }

    @Test
    void should_have_access_to_the_database() {
        // GIVEN
        final var jooqService = appCtx.jooqService;
        final var testTable = table(name("tbl_test"));
        final var testFieldId = field(name("test_id"), SQLDataType.INTEGER);
        final var testFieldName = field(name("test_name"), SQLDataType.VARCHAR(50));

        // WHEN
        // - Create temporary table
        final var createResult = jooqService.create()
                .createTemporaryTableIfNotExists(testTable)
                .column(testFieldId)
                .column(testFieldName)
                .execute();

        // - Insert
        final var insertResult = jooqService.create()
                .insertInto(testTable, testFieldId, testFieldName)
                .values(1, "Name 1")
                .values(2, "Name 2")
                .values(3, "Name 3")
                .execute();

        // - Select
        final var selectResult = jooqService.create()
                .select(testFieldId, testFieldName)
                .from(testTable)
                .fetch();

        // - Update
        final var updateResult = jooqService.create()
                .update(testTable)
                .set(testFieldName, "Updated 2")
                .where(testFieldId.eq(2))
                .returningResult(testFieldId, testFieldName)
                .fetchSingle();

        // - Delete
        final var deleteResult = jooqService.create()
                .deleteFrom(testTable)
                .where(testFieldId.lessThan(10))
                .execute();

        // - Drop temporary table
        final var dropResult = jooqService.create()
                .dropTemporaryTable(testTable)
                .execute();

        // THEN
        assertThat(createResult).isEqualTo(0);
        assertThat(insertResult).isEqualTo(3);
        assertThat(selectResult)
                .hasSize(3)
                .extracting(Record2::value1, Record2::value2)
                .containsExactly(
                        Tuple.tuple(1, "Name 1"),
                        Tuple.tuple(2, "Name 2"),
                        Tuple.tuple(3, "Name 3")
                );
        assertThat(updateResult)
                .extracting(Record2::value1, Record2::value2)
                .containsExactly(2, "Updated 2");
        assertThat(deleteResult).isEqualTo(3);
        assertThat(dropResult).isEqualTo(0);
    }

}