package de.lpo.todo.domain.utils;

/**
 * Common interface for domain objects, for they all provide IDs.
 * Is primarily used for making a domain object referencable.
 */
public interface HasId<T> {
    /**
     * @return the ID of the domain object
     */
    Id<T> id();
}
