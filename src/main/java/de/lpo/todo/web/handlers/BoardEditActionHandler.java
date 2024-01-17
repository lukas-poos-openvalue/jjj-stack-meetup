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
import org.jetbrains.annotations.Nullable;

/**
 * Performs edition of a given board, then responds the HTML of the board item
 */
public class BoardEditActionHandler extends JteHandler {
    private final JteService jteService;
    private final UserService userService;
    private final BoardService boardService;

    public BoardEditActionHandler(JteService jteService, UserService userService, BoardService boardService) {
        this.jteService = jteService;
        this.userService = userService;
        this.boardService = boardService;
    }

    @Override
    protected @Nullable JteModel provideModel(@NotNull Context ctx) {
        // Read submitted data
        if (!ctx.formParamMap().containsKey("title") || !ctx.formParamMap().containsKey("members")) {
            throw new BadRequestResponse("Endpoint expects two form params: title and members!");
        }
        final var updatedTitle = ctx.formParam("title");
        final var updatedMemberIds = ctx.formParams("members").stream().map(StringId::ofHash).toList();

        // [PoI] Update board
        final var boardId = getBoardIdFromPath(ctx);
        final var currentUser = userService.getFromContext(ctx);
        final var board = boardService.updateFetchAll(boardId, updatedTitle, updatedMemberIds);

        // Respond updated board
        return jteService.getTemplates().boardsBoardItem(currentUser, board);
    }
}
