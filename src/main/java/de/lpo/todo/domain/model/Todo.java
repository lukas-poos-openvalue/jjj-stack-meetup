package de.lpo.todo.domain.model;

import de.lpo.todo.domain.utils.HasId;
import de.lpo.todo.domain.utils.Id;
import de.lpo.todo.domain.utils.Ref;
import org.jetbrains.annotations.NotNull;

/**
 * A simple task on a {@link Board}.
 */
public record Todo(@NotNull Id<Integer> id,
                   @NotNull String content,
                   @NotNull Boolean completed,
                   @NotNull Ref<Integer, Board> board) implements HasId<Integer> {
}
