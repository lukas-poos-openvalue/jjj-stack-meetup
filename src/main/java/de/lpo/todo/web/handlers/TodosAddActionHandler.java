package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.TodoService;
import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Perform addition of todo, then responds HTML of todo item
 */
public class TodosAddActionHandler extends JteHandler {
    private final JteService jteService;
    private final TodoService todoService;

    public TodosAddActionHandler(JteService jteService, TodoService todoService) {
        this.jteService = jteService;
        this.todoService = todoService;
    }

    @Override
    protected @Nullable JteModel provideModel(@NotNull Context ctx) {
        // Read submitted content
        final var content = ctx.formParam("content");
        if (StringUtils.isBlank(content)) {
            ctx.status(HttpStatus.BAD_REQUEST).result("'content' is empty!");
            return null;
        }

        // Add to database
        final var boardId = getBoardIdFromPath(ctx);
        final var todo = todoService.addFetchNone(boardId, content);

        // Render response
        return jteService.getTemplates().todosTodoItem(todo, false);
    }
}
