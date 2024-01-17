package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.BoardService;
import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Responds the HTML of the todos page.
 */
public class TodosPageHandler extends JteHandler {
    private final JteService jteService;
    private final BoardService boardService;

    public TodosPageHandler(JteService jteService, BoardService boardService) {
        this.jteService = jteService;
        this.boardService = boardService;
    }

    @Override
    protected @Nullable JteModel provideModel(@NotNull Context ctx) {
        final var boardId = getBoardIdFromPath(ctx);
        final var board = boardService.getOneByIdFetchAll(boardId);
        return jteService.getTemplates().todos(board);
    }
}
