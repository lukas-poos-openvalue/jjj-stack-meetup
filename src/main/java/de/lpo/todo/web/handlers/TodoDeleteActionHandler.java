package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.TodoService;
import de.lpo.todo.web.JteHandler;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Performs deletion of the given todo, then responds the empty HTML of the deleted todo item
 */
public class TodoDeleteActionHandler extends JteHandler {
    private final TodoService todoService;

    public TodoDeleteActionHandler(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    protected @Nullable JteModel provideModel(@NotNull Context ctx) {
        final var todoId = getTodoIdFromPath(ctx);
        todoService.deleteById(todoId);
        return emptyModel();
    }
}