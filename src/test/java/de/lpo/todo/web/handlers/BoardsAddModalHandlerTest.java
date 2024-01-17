package de.lpo.todo.web.handlers;

import de.lpo.todo.auth.model.Role;
import de.lpo.todo.domain.UserService;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.StringId;
import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BoardsAddModalHandlerTest {
    private final JteService jteService = mock();
    private final UserService userService = mock();
    private final BoardsAddModalHandler handler = new BoardsAddModalHandler(jteService, userService);

    @Test
    void should_load_user_options_and_respond_add_board_modal_html() {
        // GIVEN
        final Context ctx = mock();

        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        final var userId = StringId.ofValue("a");
        final var user = new User(userId, "a", Role.ROLE_USER);
        when(userService.getFromContext(any())).thenReturn(user);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).boardsModalsBoardAddModal(any());
        verify(userService).getFromContext(ctx);
        verify(userService).findAllUsersExcluding(userId);
        verifyNoMoreInteractions(userService);
    }
}