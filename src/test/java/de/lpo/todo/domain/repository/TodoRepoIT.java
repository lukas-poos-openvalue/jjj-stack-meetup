package de.lpo.todo.domain.repository;

import de.lpo.todo.domain.model.Todo;
import de.lpo.utils.IntegrationTestDomain;
import de.lpo.utils.DomainTestUtils;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class TodoRepoIT extends IntegrationTestDomain {

    @Test
    void should_add() {
        // GIVEN
        // - Test-Scenario
        final var board = DomainTestUtils.persistScenarioOneBoardTwoUsers(appCtx);

        // - Domain-Object to create
        final var content = RandomStringUtils.random(10);

        // WHEN
        final var result = appCtx.todoRepo.add(board.id(), content);

        // THEN
        assertThat(result).isNotNull();
    }

    @Test
    void should_find_by_id_fetching_board() {
        // GIVEN
        // - Test-Scenario
        final var todo = DomainTestUtils.persistScenarioOneTodoOneBoardTwoUsers(appCtx);
        final var todoId = Objects.requireNonNull(todo.id());

        // WHEN
        final var result = appCtx.todoRepo.findOneByIdFetchNone(todoId);

        // THEN
        assertThat(result)
                .isPresent()
                .get()
                .extracting(Todo::content, Todo::completed, t -> t.board().isLoaded())
                .containsExactly(todo.content(), todo.completed(), false);
    }

    @Test
    void should_update() {
        // GIVEN
        // - Test-Scenario
        final var todo = DomainTestUtils.persistScenarioOneTodoOneBoardTwoUsers(appCtx);

        // - Domain-Object to update
        final var content = RandomStringUtils.random(10);
        final var completed = true;

        // WHEN
        final var result = appCtx.todoRepo.updateFetchNone(todo.id(), content, completed);

        // THEN
        assertThat(result)
                .extracting(Todo::id, Todo::content, Todo::completed, t -> t.board().isLoaded())
                .containsExactly(todo.id(), content, completed, false);
    }

    @Test
    void should_delete() {
        // GIVEN
        // - Test-Scenario
        final var todo = DomainTestUtils.persistScenarioOneTodoOneBoardTwoUsers(appCtx);
        final var todoId = todo.id();

        // WHEN
        appCtx.todoRepo.deleteById(todoId);

        // THEN
        assertThat(appCtx.todoRepo.findOneByIdFetchNone(todoId)).isEmpty();
    }
}