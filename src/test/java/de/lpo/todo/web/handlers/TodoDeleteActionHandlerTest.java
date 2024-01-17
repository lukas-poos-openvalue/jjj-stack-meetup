package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.TodoService;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TodoDeleteActionHandlerTest {
    private final TodoService todoService = mock();
    private final TodoDeleteActionHandler handler = new TodoDeleteActionHandler(todoService);

    @Test
    void should_delete_todo_and_respond_empty_html() {
        // GIVEN
        final var todoId = IntId.ofValue(123);
        final Context ctx = mock();
        WebTestUtils.mockPathParamTodoId(ctx, todoId);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html("");
        verify(todoService).deleteById(todoId);
        verifyNoMoreInteractions(todoService);
    }
}