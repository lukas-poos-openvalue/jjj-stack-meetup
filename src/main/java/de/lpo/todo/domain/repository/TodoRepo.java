package de.lpo.todo.domain.repository;

import de.lpo.todo.database.JooqService;
import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.model.Todo;
import de.lpo.todo.domain.utils.Id;
import de.lpo.todo.domain.utils.IntId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static org.jooq.generated.Tables.TBL_TODO;
import static org.jooq.impl.DSL.val;

/**
 * [PoI] A repository to encapsulate database access in order to manage {@link Todo} objects.
 * These operations a classic CRUD operations.
 */
public class TodoRepo {
    private final JooqService jooqService;
    private final TodoMapper todoMapper;

    public TodoRepo(JooqService jooqService, TodoMapper todoMapper) {
        this.jooqService = jooqService;
        this.todoMapper = todoMapper;
    }

    /**
     * Adds a new {@link Todo} to the database.
     * @param boardId the id of the {@link Board}
     * @param content the initial content
     * @return the id of the created {@link Todo}
     */
    public Id<Integer> add(@NotNull Id<Integer> boardId, @NotNull String content) {
        return jooqService.create()
                .insertInto(TBL_TODO)
                .set(TBL_TODO.BOARD_ID, boardId.value())
                .set(TBL_TODO.CONTENT, content)
                .set(TBL_TODO.COMPLETED, false)
                .returning(TBL_TODO.TODO_ID)
                .fetchSingle(r -> IntId.ofValue(r.getTodoId()));
    }

    /**
     * Selects one {@link Todo} by the given id.
     * @param todoId the id of the {@link Todo}
     * @return the value of the {@link Todo} found (excluding the referenced {@link Board}), or an empty result
     */
    public Optional<Todo> findOneByIdFetchNone(Id<Integer> todoId) {
        return jooqService.create()
                .selectFrom(TBL_TODO)
                .where(TBL_TODO.TODO_ID.eq(todoId.value()))
                .fetchOptional(todoMapper::asDto);
    }

    /**
     * Updates the {@link Todo} with the provided values.
     * @param todoId the id of the {@link Todo}
     * @param content the new content value (optional; if null it's left unchanged)
     * @param completed the new completed value (optional; if null it's left unchanged)
     * @return the {@link Todo} with all the updated values
     */
    public Todo updateFetchNone(Id<Integer> todoId, @Nullable String content, @Nullable Boolean completed) {
        return jooqService.create()
                .update(TBL_TODO)
                .set(TBL_TODO.CONTENT, content != null ? val(content) : TBL_TODO.CONTENT)
                .set(TBL_TODO.COMPLETED, completed != null ? val(completed) : TBL_TODO.COMPLETED)
                .where(TBL_TODO.TODO_ID.eq(todoId.value()))
                .returning()
                .fetchSingle(todoMapper::asDto);
    }

    /**
     * Deletes a {@link Todo} by the given id.
     * @param todoId the id of the {@link Todo}
     */
    public void deleteById(Id<Integer> todoId) {
        jooqService.create()
                .delete(TBL_TODO)
                .where(TBL_TODO.TODO_ID.eq(todoId.value()))
                .execute();
    }
}
