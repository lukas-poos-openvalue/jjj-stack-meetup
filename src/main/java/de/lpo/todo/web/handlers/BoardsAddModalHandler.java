package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.UserService;
import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

/**
 * Responds the HTML of the modal for adding a board.
 */
public class BoardsAddModalHandler extends JteHandler {
    private final JteService jteService;
    private final UserService userService;

    public BoardsAddModalHandler(JteService jteService, UserService userService) {
        this.jteService = jteService;
        this.userService = userService;
    }

    @Override
    protected JteModel provideModel(@NotNull Context ctx) {
        final var currentUser = userService.getFromContext(ctx);
        final var otherUsers = userService.findAllUsersExcluding(currentUser.id());
        return jteService.getTemplates().boardsModalsBoardAddModal(otherUsers);
    }
}
