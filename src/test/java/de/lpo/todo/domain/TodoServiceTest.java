package de.lpo.todo.domain;

import de.lpo.todo.domain.model.Todo;
import de.lpo.todo.domain.repository.TodoRepo;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.todo.domain.utils.Ref;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TodoServiceTest {
    private final TodoRepo todoRepo = mock();
    private final TodoService todoService = new TodoService(todoRepo);

    private static Todo oneTodo(int id) {
        return new Todo(IntId.ofValue(id), "content", true, Ref.unloaded(IntId.ofValue(1)));
    }

    @Test
    void should_add() {
        // GIVEN
        // - Add data
        final var boardId = IntId.ofValue(RandomUtils.nextInt());
        final var content = RandomStringUtils.random(10);

        // - Mock repo
        final var newTodoId = IntId.ofValue(RandomUtils.nextInt());
        when(todoRepo.add(any(), any())).thenReturn(newTodoId);

        // WHEN
        final var result = todoService.addFetchNone(boardId, content);

        // THEN
        assertThat(result)
                .extracting(Todo::id, Todo::content, Todo::completed, t -> t.board().id())
                .containsExactly(newTodoId, content, false, boardId);
        verify(todoRepo).add(boardId, content);
    }

    @Test
    void should_find_by_id() {
        // GIVEN
        // - Existing Todo
        final var todo = oneTodo(RandomUtils.nextInt());

        // - Mock repo
        when(todoRepo.findOneByIdFetchNone(any())).thenReturn(Optional.of(todo));

        // WHEN
        final var result = todoService.getOneByIdFetchNone(todo.id());

        // THEN
        assertThat(result).isEqualTo(todo);
        verify(todoRepo).findOneByIdFetchNone(todo.id());
    }

    @Test
    void should_update() {
        // GIVEN
        // - Existing Todo
        final var todo = oneTodo(RandomUtils.nextInt());

        // - Mock repo
        final var updatedContent = RandomStringUtils.random(10);
        final var updatedCompleted = true;
        final var updatedTodo = new Todo(todo.id(), updatedContent, updatedCompleted, todo.board());
        when(todoRepo.updateFetchNone(any(), any(), any())).thenReturn(updatedTodo);

        // WHEN
        final var result = todoService.updateFetchNone(todo.id(), updatedContent, updatedCompleted);

        // THEN
        assertThat(result).isEqualTo(updatedTodo);
        verify(todoRepo).updateFetchNone(todo.id(), updatedContent, updatedCompleted);
    }

    @Test
    void should_delete() {
        // GIVEN
        final var todoId = IntId.ofValue(RandomUtils.nextInt());

        // WHEN
        todoService.deleteById(todoId);

        // THEN
        verify(todoRepo).deleteById(todoId);
    }
}
