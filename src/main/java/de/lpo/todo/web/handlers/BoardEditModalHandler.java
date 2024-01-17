package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.BoardService;
import de.lpo.todo.domain.UserService;
import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Responds the HTML of modal for editing a given board
 */
public class BoardEditModalHandler extends JteHandler {
    private final JteService jteService;
    private final UserService userService;
    private final BoardService boardService;

    public BoardEditModalHandler(JteService jteService, UserService userService, BoardService boardService) {
        this.jteService = jteService;
        this.userService = userService;
        this.boardService = boardService;
    }

    @Override
    protected @Nullable JteModel provideModel(@NotNull Context ctx) {
        // Fetch board
        final var boardId = getBoardIdFromPath(ctx);
        final var board = boardService.getOneByIdFetchAll(boardId);

        // Determine other users
        final var currentUser = userService.getFromContext(ctx);
        final var otherUsers = userService.findAllUsersExcluding(currentUser.id());

        // Return HTML for modal
        return jteService.getTemplates().boardsModalsBoardEditModal(board, otherUsers);
    }
}
