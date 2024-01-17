package de.lpo.todo.domain.utils;

import org.jetbrains.annotations.NotNull;

/**
 * A reference to another domain object. The reference can have two states:
 * <ul>
 *     <li>Loaded: You can access the ids and the referenced domain object as well</li>
 *     <li>Unloaded: You only have access to the ids.</li>
 * </ul>
 *
 * @param <ID> The type of the ID's value (usually {@link String} or {@link Integer})
 * @param <T>  The type of the referenced domain object
 */
public sealed interface Ref<ID, T extends HasId<ID>> {
    /**
     * @param value the referenced domain object
     * @return a loaded reference, providing the referenced domain object
     */
    static <ID, T extends HasId<ID>> Ref<ID, T> loaded(@NotNull T value) {
        return new Loaded<>(value);
    }

    /**
     * @param id the technical id of the referenced object
     * @return an unloaded reference, providing only IDs
     */
    static <ID, T extends HasId<ID>> Ref<ID, T> unloaded(Id<ID> id) {
        return new Unloaded<>(id);
    }

    /**
     * @return the technical ID of the referenced object
     */
    Id<ID> id();

    /**
     * @return <code>true</code> if the reference is loaded, hence if the referenced domain object can be obtained
     */
    default boolean isLoaded() {
        return this instanceof Ref.Loaded<ID, T>;
    }

    /**
     * @return The referenced domain object if loaded, or throw an error otherwise.
     * For safe access, use pattern matching instead!
     */
    @NotNull
    default T unwrap() {
        if (this instanceof Ref.Loaded<ID, T> loaded) {
            return loaded.value;
        }
        throw new IllegalStateException("Cannot get value from unloaded reference!");
    }

    /**
     * A loaded reference, containing the referenced domain object
     */
    record Loaded<ID, T extends HasId<ID>>(@NotNull T value) implements Ref<ID, T> {
        public Id<ID> id() {
            return value.id();
        }
    }

    /**
     * An unloaded reference, containing only the ids of the referenced domain object
     */
    record Unloaded<ID, T extends HasId<ID>>(Id<ID> id) implements Ref<ID, T> {
    }
}
