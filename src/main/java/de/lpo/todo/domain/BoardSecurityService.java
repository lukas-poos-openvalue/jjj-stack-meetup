package de.lpo.todo.domain;

import de.lpo.todo.domain.utils.IntId;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;

public class BoardSecurityService {
    private final UserService userService;

    public BoardSecurityService(UserService userService) {
        this.userService = userService;
    }


    /**
     * This handler checks if a board is references in the path, and then checks if the requesting user
     * can have access to that resource. A user may have access to a board if they are either the owner
     * or a member of the board.
     *
     * @return the board access handler
     */
    public Handler handleBoardAccess() {
        return ctx -> {
            // Path param for hashed board ID is required!
            final var boardId = IntId.ofHash(ctx.pathParam("board-id-hashed"));
            if (boardId == null) {
                throw new UnauthorizedResponse("Cannot access board without an ID!");
            }

            // [PoI] Get the ids for the board and the current user, then check the access
            final var currentUser = userService.getFromContext(ctx);
            if (!userService.canUserAccessBoard(currentUser.id(), boardId)) {
                throw new UnauthorizedResponse("Cannot access board with ID '" + boardId + "'!");
            }
        };
    }
}
