package de.lpo.todo.web.handlers;

import de.lpo.todo.auth.model.Role;
import de.lpo.todo.domain.BoardService;
import de.lpo.todo.domain.UserService;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.StringId;
import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BoardsPageHandlerTest {
    private final JteService jteService = mock();
    private final UserService userService = mock();
    private final BoardService boardService = mock();
    private final BoardsPageHandler handler = new BoardsPageHandler(jteService, userService, boardService);

    @Test
    void should_load_boards_and_respond_boards_page() {
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
        verify(templates).boards(eq(user), any());
        verify(userService).getFromContext(ctx);
        verify(boardService).findAllForUserFetchAll(user);
        verifyNoMoreInteractions(userService, boardService);
    }
}