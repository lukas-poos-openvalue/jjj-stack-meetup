package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.TodoService;
import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Responds the HTML of the todo item
 */
public class TodoItemHandler extends JteHandler {
    private final JteService jteService;
    private final TodoService todoService;

    public TodoItemHandler(JteService jteService, TodoService todoService) {
        this.jteService = jteService;
        this.todoService = todoService;
    }

    @Override
    protected @Nullable JteModel provideModel(@NotNull Context ctx) {
        final var todoId = getTodoIdFromPath(ctx);
        final var todo = todoService.getOneByIdFetchNone(todoId);
        final var editing = "true".equals(ctx.queryParam("edit"));
        return jteService.getTemplates().todosTodoItem(todo, editing);
    }
}