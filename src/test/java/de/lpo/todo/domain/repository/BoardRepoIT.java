package de.lpo.todo.domain.repository;

import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.model.Todo;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.Id;
import de.lpo.utils.IntegrationTestDomain;
import de.lpo.utils.DomainTestUtils;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

class BoardRepoIT extends IntegrationTestDomain {

    @Test
    void should_add() {
        // GIVEN
        // - Test-Scenario
        final var users = DomainTestUtils.persistScenarioThreeUsers(appCtx);

        // - Board data
        final var title = RandomStringUtils.random(10);
        final var ownerId = users.getFirst().id();
        final var memberIds = List.of(
                users.getLast().id()
        );

        // WHEN
        final var result = appCtx.boardRepo.add(title, ownerId, memberIds);

        // THEN
        assertThat(result).isNotNull();
    }

    @Test
    void should_find_one_by_id_fetching_all() {
        // GIVEN
        // - Test-Scenario
        final var todo = DomainTestUtils.persistScenarioOneTodoOneBoardTwoUsers(appCtx);
        final var boardId = requireNonNull(todo.board()).id();

        // WHEN
        final var result = appCtx.boardRepo.findOneByIdFetchAll(boardId);

        // THEN
        final var assertBoard = assertThat(result)
                .isPresent()
                .get()
                .satisfies(b -> assertThat(b)
                        .extracting(Board::title)
                        .isEqualTo("title")
                );
        assertBoard
                .extracting(Board::owner)
                .extracting(DomainTestUtils::extractRef)
                .extracting(User::displayName)
                .isEqualTo("displayNameA");
        assertBoard
                .extracting(Board::members)
                .extracting(DomainTestUtils::extractRefs, list(User.class))
                .hasSize(1)
                .first()
                .extracting(User::displayName)
                .isEqualTo("displayNameB");
        assertBoard
                .extracting(Board::todos)
                .extracting(DomainTestUtils::extractRefs, list(Todo.class))
                .hasSize(1)
                .first()
                .extracting(Todo::id, Todo::content, Todo::content, Todo::completed)
                .containsExactly(todo.id(), todo.content(), todo.content(), todo.completed());
    }

    @Test
    void should_find_all_for_owner() {
        // GIVEN
        // - Test-Scenario
        final var board = DomainTestUtils.persistScenarioOneBoardTwoUsers(appCtx);
        final var owner = board.owner().unwrap();

        // WHEN
        final var result = appCtx.boardRepo.findAllForUserFetchAll(owner);

        // THEN
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(
                        Board::id,
                        Board::title,
                        x -> x.todos().unwrap().size(),
                        x -> x.owner().unwrap().displayName(),
                        x -> x.members().unwrap().getFirst().displayName()
                )
                .containsExactly(
                        board.id(), board.title(), 0, "displayNameA", "displayNameB"
                );
    }

    @Test
    void should_find_all_for_member() {
        // GIVEN
        // - Test-Scenario
        final var board = DomainTestUtils.persistScenarioOneBoardTwoUsers(appCtx);
        final var member = board.members().unwrap().getFirst();

        // WHEN
        final var result = appCtx.boardRepo.findAllForUserFetchAll(member);

        // THEN
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(
                        Board::id,
                        Board::title,
                        x -> x.todos().unwrap().size(),
                        x -> x.owner().unwrap().displayName(),
                        x -> x.members().unwrap().getFirst().displayName()
                )
                .containsExactly(
                        board.id(), board.title(), 0, "displayNameA", "displayNameB"
                );
    }

    @Test
    void should_update() {
        // GIVEN
        // - Test-Scenario
        final var board = DomainTestUtils.persistScenarioOneBoardTwoUsers(appCtx);

        // - Updated board
        final var title = RandomStringUtils.random(10);
        final List<Id<String>> memberIds = List.of();

        // WHEN
        appCtx.boardRepo.update(board.id(), title, memberIds);

        // THEN
    }

    @Test
    void should_delete_by_id() {
        // GIVEN
        // - Test-scenario
        final var board = DomainTestUtils.persistScenarioOneBoardTwoUsers(appCtx);
        final var boardId = board.id();

        // WHEN
        appCtx.boardRepo.deleteById(boardId);

        // THEN
        assertThat(appCtx.boardRepo.findOneByIdFetchAll(boardId)).isEmpty();
    }
}