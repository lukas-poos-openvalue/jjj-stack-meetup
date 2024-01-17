package de.lpo.todo.dependencies;

/**
 * Common interface for all services that need to be initialized manually.
 * <ul>
 *     <li>
 *         Note that dependencies of a service might need to be initialized before initializing
 *         the service itself!
 *     </li>
 *     <li>The init-method doesn't have to support repeatable calls. Subsequent calls may result in errors!</li>
 * </ul>
 * </p>
 */
public interface Initializable {
    /**
     * Initializes a service's state. Doesn't have to be repeatable.
     */
    void init();
}
