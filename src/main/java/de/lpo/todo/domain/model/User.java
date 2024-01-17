package de.lpo.todo.domain.model;

import de.lpo.todo.auth.model.Role;
import de.lpo.todo.domain.utils.HasId;
import de.lpo.todo.domain.utils.Id;
import org.jetbrains.annotations.NotNull;

/**
 * Domain-representation of {@link de.lpo.todo.auth.model.AuthUser}.
 */
public record User(@NotNull Id<String> id,
                   @NotNull String displayName,
                   @NotNull Role role) implements HasId<String> {
}
