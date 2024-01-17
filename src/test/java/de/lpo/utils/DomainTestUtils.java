package de.lpo.utils;

import de.lpo.todo.auth.model.AuthUser;
import de.lpo.todo.auth.model.Role;
import de.lpo.todo.dependencies.ApplicationContext;
import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.model.Todo;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.HasId;
import de.lpo.todo.domain.utils.Ref;
import de.lpo.todo.domain.utils.Refs;
import org.assertj.core.api.Assertions;

import java.util.List;

public final class DomainTestUtils {

    public static List<User> persistScenarioThreeUsers(ApplicationContext appCtx) {
        final var authUsers = List.of(
                new AuthUser("userIdA", "userNameA", "emailA", "displayNameA", Role.ROLE_USER),
                new AuthUser("userIdB", "userNameB", "emailB", "displayNameB", Role.ROLE_USER),
                new AuthUser("userIdC", "userNameC", "emailC", "displayNameC", Role.ROLE_USER)
        );
        authUsers.forEach(appCtx.authUserService::save);
        return authUsers.stream().map(appCtx.userMapper::asDto).toList();
    }

    public static Board persistScenarioOneBoardTwoUsers(ApplicationContext appCtx) {
        final var authUsers = List.of(
                new AuthUser("userIdA", "userNameA", "emailA", "displayNameA", Role.ROLE_USER),
                new AuthUser("userIdB", "userNameB", "emailB", "displayNameB", Role.ROLE_USER)
        );
        authUsers.forEach(appCtx.authUserService::save);
        final var userA = appCtx.userMapper.asDto(authUsers.get(0));
        final var userB = appCtx.userMapper.asDto(authUsers.get(1));

        return appCtx.boardService.addFetchAll("title", userA.id(), List.of(userB.id()));
    }

    public static Todo persistScenarioOneTodoOneBoardTwoUsers(ApplicationContext appCtx) {
        final var board = persistScenarioOneBoardTwoUsers(appCtx);
        final var todo = appCtx.todoService.addFetchNone(board.id(), "content");
        return new Todo(todo.id(), todo.content(), todo.completed(), Ref.loaded(board));
    }

    public static <ID, T extends HasId<ID>> T extractRef(Ref<ID, T> ref) {
        return switch (ref) {
            case Ref.Loaded(var value) -> value;
            default -> Assertions.fail("Ref should have been loaded!");
        };
    }

    public static <T extends HasId<?>> List<T> extractRefs(Refs<T> refs) {
        return switch (refs) {
            case Refs.Loaded(var values) -> values;
            default -> Assertions.fail("Refs should have been loaded!");
        };
    }
}
