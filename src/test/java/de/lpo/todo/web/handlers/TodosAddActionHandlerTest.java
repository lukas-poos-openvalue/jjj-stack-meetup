package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.TodoService;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TodosAddActionHandlerTest {
    private final JteService jteService = mock();
    private final TodoService todoService = mock();
    private final TodosAddActionHandler handler = new TodosAddActionHandler(jteService, todoService);

    @Test
    void should_add_todo_and_respond_todo_html() {
        // GIVEN
        // - Templates
        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        // - Context
        final Context ctx = mock();
        final var boardId = IntId.ofValue(123);
        WebTestUtils.mockPathParamBoardId(ctx, IntId.ofValue(123));
        when(ctx.formParam("content")).thenReturn("content");

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).todosTodoItem(any(), eq(false));
        verify(todoService).addFetchNone(boardId, "content");
        verifyNoMoreInteractions(todoService);
    }
}