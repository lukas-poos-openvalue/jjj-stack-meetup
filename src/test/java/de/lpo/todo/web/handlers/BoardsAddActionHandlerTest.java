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

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class BoardsAddActionHandlerTest {
    private final JteService jteService = mock();
    private final UserService userService = mock();
    private final BoardService boardService = mock();
    private final BoardsAddActionHandler handler = new BoardsAddActionHandler(jteService, userService, boardService);

    @Test
    void should_add_board_and_respond_board_html() {
        // GIVEN
        // - Templates
        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        // - UserService
        final var userId = StringId.ofValue("a");
        final var user = new User(userId, "a", Role.ROLE_USER);
        when(userService.getFromContext(any())).thenReturn(user);

        // - Context
        final Context ctx = mock();
        when(ctx.formParamMap()).thenReturn(Map.of("title", List.of("title")));
        when(ctx.formParam("title")).thenReturn("title");


        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).boardsBoardItem(eq(user), any());
        verify(userService).getFromContext(ctx);
        verify(boardService).addFetchAll("title", userId, List.of());
        verifyNoMoreInteractions(userService, boardService);
    }
}