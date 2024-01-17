package de.lpo.todo.config.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Supported profiles for this application.
 */
public enum Profile {
    DEV, NONE;

    public static Optional<Profile> findValueOf(String name) {
        return Arrays.stream(values()).filter(e -> e.name().equals(name)).findAny();
    }
}
