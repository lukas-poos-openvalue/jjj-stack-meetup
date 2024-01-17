package de.lpo.todo.auth.model;

import io.javalin.security.RouteRole;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * The available roles that an authenticated user can have.
 */
public enum Role implements RouteRole {
    ROLE_ADMIN, ROLE_USER;

    /**
     * @return the first role that can be found in the given collection of names
     */
    public static Optional<Role> tryFirstValueOf(@NotNull Collection<String> names) {
        return names.stream()
                .flatMap(r -> tryValueOf(r).stream())
                .min(Comparator.comparing(Role::ordinal));
    }

    /**
     * @return like {@link #valueOf(String)}, but without the exception
     */
    public static Optional<Role> tryValueOf(String name) {
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }
        return Arrays.stream(values()).filter(v -> v.name().equals(name)).findFirst();
    }
}
