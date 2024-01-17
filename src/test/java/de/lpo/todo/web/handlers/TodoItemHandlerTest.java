package de.lpo.todo.web.handlers;

import de.lpo.todo.domain.TodoService;
import de.lpo.todo.domain.model.Todo;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.todo.domain.utils.Ref;
import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TodoItemHandlerTest {
    private final JteService jteService = mock();
    private final TodoService todoService = mock();
    private final TodoItemHandler handler = new TodoItemHandler(jteService, todoService);

    @Test
    void should_respond_todo_html() {
        // GIVEN
        // - Templates and how they render
        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        // - Todoitem
        final var boardId = IntId.ofValue(1);
        final var todoId = IntId.ofValue(1);
        final var todo = new Todo(todoId, "content", false, Ref.unloaded(boardId));
        when(todoService.getOneByIdFetchNone(any())).thenReturn(todo);

        // - Context
        final Context ctx = mock();
        WebTestUtils.mockPathParamTodoId(ctx, todoId);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).todosTodoItem(todo, false);
        verify(todoService).getOneByIdFetchNone(todoId);
        verifyNoMoreInteractions(todoService);
    }
}