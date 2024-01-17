package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.TodoService;
import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Perfoms edition of the given todo, then responds HTML of todo item.
 */
public class TodoEditActionHandler extends JteHandler {
    private final JteService jteService;
    private final TodoService todoService;

    public TodoEditActionHandler(JteService jteService, TodoService todoService) {
        this.jteService = jteService;
        this.todoService = todoService;
    }

    @Override
    protected @Nullable JteModel provideModel(@NotNull Context ctx) {
        // Read submitted content and completed flag
        final var newContent = ctx.formParam("content");
        final var newCompletedStr = ctx.formParam("completed");
        final var newCompleted = newCompletedStr != null ? Boolean.parseBoolean(newCompletedStr) : null;

        // Edit todo
        final var todoId = getTodoIdFromPath(ctx);
        final var todo = todoService.updateFetchNone(todoId, newContent, newCompleted);

        // Respond updated todo item
        return jteService.getTemplates().todosTodoItem(todo, false);
    }
}