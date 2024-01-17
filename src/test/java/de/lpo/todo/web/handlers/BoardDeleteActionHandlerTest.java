package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.BoardService;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BoardDeleteActionHandlerTest {
    private final BoardService boardService = mock();
    private final BoardDeleteActionHandler handler = new BoardDeleteActionHandler(boardService);

    @Test
    void should_delete_board_and_respond_empty_html() {
        // GIVEN
        final var boardId = IntId.ofValue(1);
        final Context ctx = mock();
        WebTestUtils.mockPathParamBoardId(ctx, boardId);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html("");
        verify(boardService).deleteById(boardId);
        verifyNoMoreInteractions(boardService);
    }
}