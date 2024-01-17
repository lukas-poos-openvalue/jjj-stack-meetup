/**
 * <h1>Module 'auth'</h1>
 * <p>
 * Provides services that help to manage authentication.
 * </p>
 * <p>
 * The authenticated user data usually is provided by the OIDC-profiles on successful authentication.
 * It then can be used to assign domain objects to specific users.
 * </p>
 * <p>
 * Note that in order for the integration of Pac4j into Javalin to work, some workarounds need to be applied.
 * Check comments on [PoI] markers for more details.
 * </p>
 */
package de.lpo.todo.auth;