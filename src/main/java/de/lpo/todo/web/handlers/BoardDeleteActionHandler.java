package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.BoardService;
import de.lpo.todo.web.JteHandler;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Performs deletion of given board, then responds empty HTML of the deleted board item.
 */
public class BoardDeleteActionHandler extends JteHandler {
    private final BoardService boardService;

    public BoardDeleteActionHandler(BoardService boardService) {
        this.boardService = boardService;
    }

    @Override
    protected @Nullable JteModel provideModel(@NotNull Context ctx) {
        final var boardId = getBoardIdFromPath(ctx);
        boardService.deleteById(boardId);
        return emptyModel();
    }
}
