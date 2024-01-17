package de.lpo.todo.domain.repository;

import de.lpo.todo.domain.model.Todo;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.todo.domain.utils.Ref;
import org.jooq.generated.tables.records.TblTodoRecord;

/**
 * A service that provides mapping utilities between {@link Todo todos} and their table record representation.
 */
public class TodoMapper {

    /**
     *
     * @param todoRecord the {@link TblTodoRecord todo record}
     * @return a {@link Todo todo domain object}, filled with all the given data
     */
    public Todo asDto(TblTodoRecord todoRecord) {
        return new Todo(
                IntId.ofValue(todoRecord.getTodoId()),
                todoRecord.getContent(),
                todoRecord.getCompleted(),
                Ref.unloaded(IntId.ofValue(todoRecord.getBoardId()))
        );
    }
}
