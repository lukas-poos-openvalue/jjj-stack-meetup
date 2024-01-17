package de.lpo.todo.domain.model;

import de.lpo.todo.domain.utils.*;
import org.jetbrains.annotations.NotNull;

/**
 * A board is a collection of {@link Todo todos} that is owned by a {@link User}.
 * Boards can have multiple {@link User members}, who can access the board and its {@link Todo todos} as well.
 */
public record Board(@NotNull Id<Integer> id,
                    @NotNull String title,
                    @NotNull Refs<Todo> todos,
                    @NotNull Ref<String, User> owner,
                    @NotNull Refs<User> members) implements HasId<Integer> {
}
