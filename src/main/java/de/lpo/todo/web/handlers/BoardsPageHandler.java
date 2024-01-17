package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.BoardService;
import de.lpo.todo.domain.UserService;
import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

/**
 * Responds the HTML of the boards page.
 */
public class BoardsPageHandler extends JteHandler {
    private final JteService jteService;
    private final UserService userService;
    private final BoardService boardService;

    public BoardsPageHandler(JteService jteService, UserService userService, BoardService boardService) {
        this.jteService = jteService;
        this.userService = userService;
        this.boardService = boardService;
    }

    @Override
    protected JteModel provideModel(@NotNull Context ctx) {
        final var currentUser = userService.getFromContext(ctx);
        final var boards = boardService.findAllForUserFetchAll(currentUser);
        return jteService.getTemplates().boards(currentUser, boards);
    }
}
