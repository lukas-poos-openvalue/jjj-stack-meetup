/**
 * <h1>Module 'database'</h1>
 * <p>
 * In this module, there are three big services:
 * </p>
 * <ul>
 *     <li>
 *         {@link de.lpo.todo.database.DataSourceService}:
 *         The service provides pooled connections to the database with a {@link javax.sql.DataSource}.
 *     </li>
 *     <li>
 *         {@link de.lpo.todo.database.DbMigrationService}:
 *         Executes updates to the database using changesets defined in the resources.
 *         These updates are run on application startup.
 *     </li>
 *     <li>
 *         {@link de.lpo.todo.database.JooqService}:
 *         Provides easy access the database using the {@link org.jooq.DSLContext} from jOOQ.
 *         This DSL allows you to write type-safe queries in Java.
 *     </li>
 * </ul>
 */
package de.lpo.todo.database;