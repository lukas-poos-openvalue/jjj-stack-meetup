package de.lpo.todo.domain;

import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.model.Todo;
import de.lpo.todo.domain.repository.TodoRepo;
import de.lpo.todo.domain.utils.Id;
import de.lpo.todo.domain.utils.Ref;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Service for managing todos.
 * It provides the basic CRUD functionalities.
 */
public class TodoService {
    private final TodoRepo todoRepo;

    public TodoService(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    /**
     * Adds a {@link Todo} to the database, and returns the created object.
     * Note that the reference to the board remains unloaded.
     * @param boardId the id of the {@link Board} that this todo belongs to.
     * @param content the content of the task
     * @return the created {@link Todo} (without the board)
     */
    public Todo addFetchNone(@NotNull Id<Integer> boardId, @NotNull String content) {
        final var todoId = todoRepo.add(boardId, content);
        return new Todo(
                todoId,
                content,
                false,
                Ref.unloaded(boardId)
        );
    }

    /**
     * @param todoId the id of the {@link Todo}
     * @return the value of the {@link Todo} with the given id (without the board), or an empty value.
     */
    public Todo getOneByIdFetchNone(@NotNull Id<Integer> todoId) {
        return todoRepo.findOneByIdFetchNone(todoId).orElseThrow(TodoNotFoundException::new);
    }

    /**
     * Performs updates to the {@link Todo}.
     * @param todoId the id
     * @param content the updated content (remains unchanged when null)
     * @param completed the updated completed flag (remains unchanged when null)
     * @return the {@link Todo} with the updated values
     */
    public Todo updateFetchNone(@NotNull Id<Integer> todoId, @Nullable String content, @Nullable Boolean completed) {
        return todoRepo.updateFetchNone(todoId, content, completed);
    }

    /**
     * Deletes a {@link Todo}
     * @param todoId the id
     */
    public void deleteById(@NotNull Id<Integer> todoId) {
        todoRepo.deleteById(todoId);
    }

    public static class TodoNotFoundException extends RuntimeException {
        public TodoNotFoundException() {
            super("No todo was found with the given ID!");
        }
    }
}
