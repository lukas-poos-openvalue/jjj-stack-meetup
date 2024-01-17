package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.BoardService;
import de.lpo.todo.domain.UserService;
import de.lpo.todo.domain.utils.StringId;
import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

/**
 * Performs the creation of a new board, and then responds the HTML of the board item
 */
public class BoardsAddActionHandler extends JteHandler {
    private final JteService jteService;
    private final UserService userService;
    private final BoardService boardService;

    public BoardsAddActionHandler(JteService jteService, UserService userService, BoardService boardService) {
        this.jteService = jteService;
        this.userService = userService;
        this.boardService = boardService;
    }

    @Override
    protected JteModel provideModel(@NotNull Context ctx) {
        // Read submitted data
        if (!ctx.formParamMap().containsKey("title")) {
            throw new BadRequestResponse("Endpoint expects one form param: title!");
        }
        final var title = ctx.formParam("title");
        final var memberIdHashes = ctx.formParams("members").stream().map(StringId::ofHash).toList();

        // Create new board for current user
        final var currentUser = userService.getFromContext(ctx);
        final var board = boardService.addFetchAll(title, currentUser.id(), memberIdHashes);

        // Respond updated board
        return jteService.getTemplates().boardsBoardItem(currentUser, board);
    }
}
