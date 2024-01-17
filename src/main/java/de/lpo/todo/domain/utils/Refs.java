package de.lpo.todo.domain.utils;

import java.util.List;

/**
 * A list of references domain objects. This list of references can have two states:
 * <ul>
 *     <li>Loaded: You can access the referenced domain objects</li>
 *     <li>Unloaded: You can access nothing</li>
 * </ul>
 *
 * @param <T> The type of the referenced domain objects
 */
public sealed interface Refs<T extends HasId<?>> {
    /**
     * @param values the referenced domain objects
     * @return a loaded list of references, providing the referenced domain objects
     */
    static <T extends HasId<?>> Refs<T> loaded(List<T> values) {
        return new Loaded<>(values);
    }

    /**
     * @return an unloaded list of references, providing nothing
     */
    static <T extends HasId<?>> Refs<T> unloaded() {
        return new Unloaded<>();
    }

    /**
     * @return The referenced domain objects if loaded, or throw an error otherwise.
     * For safe access, use pattern matching instead!
     */
    default List<T> unwrap() {
        if (this instanceof Refs.Loaded<T> loaded) {
            return loaded.values;
        }
        throw new IllegalStateException("Cannot get values from unloaded references!");
    }

    /**
     * A loaded list of references, containing the referenced domain objects
     */
    record Loaded<T extends HasId<?>>(List<T> values) implements Refs<T> {
    }

    /**
     * An unloaded list of references, containing nothing
     */
    record Unloaded<T extends HasId<?>>() implements Refs<T> {
    }
}
