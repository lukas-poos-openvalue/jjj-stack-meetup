package de.lpo.todo.auth.model;

import org.jetbrains.annotations.NotNull;

/**
 * The stored user information in the database.
 */
public record AuthUser(@NotNull String userId,
                       @NotNull String userName,
                       @NotNull String email,
                       @NotNull String displayName,
                       @NotNull Role role) {
}
