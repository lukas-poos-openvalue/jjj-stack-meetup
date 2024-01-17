package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.BoardService;
import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.todo.domain.utils.Ref;
import de.lpo.todo.domain.utils.Refs;
import de.lpo.todo.domain.utils.StringId;
import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TodosPageHandlerTest {
    private final JteService jteService = mock();
    private final BoardService boardService = mock();
    private final TodosPageHandler handler = new TodosPageHandler(jteService, boardService);

    @Test
    void should_load_board_and_respond_todos_page() {
        // GIVEN
        // - Templates
        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        // - BoardService
        final var boardId = IntId.ofValue(1);
        final var ownerId = StringId.ofValue("a");
        final Board board = new Board(boardId, "title", Refs.unloaded(), Ref.unloaded(ownerId), Refs.unloaded());
        when(boardService.getOneByIdFetchAll(any())).thenReturn(board);

        // - Context
        final Context ctx = mock();
        WebTestUtils.mockPathParamBoardId(ctx, boardId);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).todos(board);
        verify(boardService).getOneByIdFetchAll(boardId);
        verifyNoMoreInteractions(boardService);
    }
}