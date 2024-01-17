package de.lpo.todo.web.handlers;

import de.lpo.todo.auth.model.Role;
import de.lpo.todo.domain.BoardService;
import de.lpo.todo.domain.UserService;
import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.todo.domain.utils.Ref;
import de.lpo.todo.domain.utils.Refs;
import de.lpo.todo.domain.utils.StringId;
import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BoardEditModalHandlerTest {
    private final JteService jteService = mock();
    private final UserService userService = mock();
    private final BoardService boardService = mock();
    private final BoardEditModalHandler handler = new BoardEditModalHandler(jteService, userService, boardService);

    @Test
    void should_respond_edit_board_modal_html() {
        // GIVEN
        // - Templates
        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        // - BoardService
        final var boardId = IntId.ofValue(1);
        final var ownerId = StringId.ofValue("a");
        final var board = new Board(boardId, "title", Refs.unloaded(), Ref.unloaded(ownerId), Refs.unloaded());
        when(boardService.getOneByIdFetchAll(any())).thenReturn(board);

        // - UserService
        final var user = new User(ownerId, "a", Role.ROLE_USER);
        when(userService.getFromContext(any())).thenReturn(user);

        // - Context
        final Context ctx = mock();
        WebTestUtils.mockPathParamBoardId(ctx, boardId);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).boardsModalsBoardEditModal(any(), any());
        verify(userService).getFromContext(ctx);
        verify(userService).findAllUsersExcluding(ownerId);
        verify(boardService).getOneByIdFetchAll(boardId);
        verifyNoMoreInteractions(userService, boardService);
    }
}