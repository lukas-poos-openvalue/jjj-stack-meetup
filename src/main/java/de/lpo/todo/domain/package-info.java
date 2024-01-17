/**
 * <h1>Module 'domain'</h1>
 * <p>
 * All the relevant business logic is located in this package.
 * Here is a quick overview on the domain:
 * </p>
 * <ul>
 *     <li>
 *         {@link de.lpo.todo.domain.model.User}:
 *         Business-view on the authorized used.
 *     </li>
 *     <li>
 *         {@link de.lpo.todo.domain.model.Board}:
 *         A collection of {@link de.lpo.todo.domain.model.Todo todos} with a title, an owner, and members that may access the board as well.
 *     </li>
 *     <li>
 *         {@link de.lpo.todo.domain.model.Todo}: A simple task on a board.
 *     </li>
 *     <li>
 *         {@link de.lpo.todo.domain.UserService}:
 *         Wraps the access to the {@link de.lpo.todo.auth.AuthUserService AuthUserService} to meet the domain's needs.
 *     </li>
 *     <li>
 *         {@link de.lpo.todo.domain.BoardService}:
 *         Allows managing {@link de.lpo.todo.domain.model.Board boards}.
 *     </li>
 *     <li>
 *         {@link de.lpo.todo.domain.TodoService}:
 *         Allows managing {@link de.lpo.todo.domain.model.Todo todos}.
 *     </li>
 *     <li>
 *         {@link de.lpo.todo.domain.repository}: Package containing all the database interactions necessary for the domain.
 *     </li>
 *     <li>
 *         {@link de.lpo.todo.domain.utils}: Util classes used for IDs and references of the domain objects.
 *     </li>
 * </ul>
 */
package de.lpo.todo.domain;