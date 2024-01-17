/**
 * <h1>Module 'config'</h1>
 * <p>
 *     This module is all about the configuration of the application.
 *     The goal is to allow the following ways of configuration:
 * </p>
 * <ul>
 *     <li>Profiles: Read a profile from the executing environment, mainly for supporting dev features in a dev environment</li>
 *     <li>Properties file: Read a properties file to get the initial configuration</li>
 *     <li>System Properties & Variables: Take system properties and environment variables into consideration</li>
 * </ul>
 * <p>
 *     For the actual reading, no external library is used, only {@link java.util.Properties}.
 *     The only downside is the translation from a properties object to the {@link de.lpo.todo.config.model.Config Config type}.
 *     The libraries that offer you the automatic mapping are big and full of reflections.
 *     This is why for this case we do it ourselves!
 * </p>
 */
package de.lpo.todo.config;

