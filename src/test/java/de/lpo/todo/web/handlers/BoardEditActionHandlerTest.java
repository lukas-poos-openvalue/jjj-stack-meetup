package de.lpo.todo.web.handlers;

import de.lpo.todo.auth.model.Role;
import de.lpo.todo.domain.BoardService;
import de.lpo.todo.domain.UserService;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.todo.domain.utils.StringId;
import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class BoardEditActionHandlerTest {
    private final JteService jteService = mock();
    private final UserService userService = mock();
    private final BoardService boardService = mock();
    private final BoardEditActionHandler handler = new BoardEditActionHandler(jteService, userService, boardService);

    @Test
    void should_edit_board_and_respond_board_html() {
        // GIVEN
        final var boardId = IntId.ofValue(123);

        // - Templates
        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        // - UserService
        final var userId = StringId.ofValue("a");
        final var user = new User(userId, "a", Role.ROLE_USER);
        when(userService.getFromContext(any())).thenReturn(user);

        // - Context
        final Context ctx = mock();
        WebTestUtils.mockPathParamBoardId(ctx, boardId);
        when(ctx.formParamMap()).thenReturn(Map.of("title", List.of("title"), "members", List.of()));
        when(ctx.formParam("title")).thenReturn("title");

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).boardsBoardItem(any(), any());
        verify(userService).getFromContext(ctx);
        verify(boardService).updateFetchAll(boardId, "title", List.of());
        verifyNoMoreInteractions(userService, boardService);
    }
}