package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.TodoService;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TodoEditActionHandlerTest {
    private final JteService jteService = mock();
    private final TodoService todoService = mock();
    private final TodoEditActionHandler handler = new TodoEditActionHandler(jteService, todoService);

    @Test
    void should_edit_todo_and_respond_todo_html() {
        // GIVEN
        final var todoId = IntId.ofValue(123);

        // - Templates
        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        // - Context
        final Context ctx = mock();
        WebTestUtils.mockPathParamTodoId(ctx, todoId);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).todosTodoItem(any(), eq(false));
        verify(todoService).updateFetchNone(todoId, null, null);
        verifyNoMoreInteractions(todoService);
    }
}