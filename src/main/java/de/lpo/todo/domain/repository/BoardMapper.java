package de.lpo.todo.domain.repository;

import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.model.Todo;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.IntId;
import de.lpo.todo.domain.utils.Ref;
import de.lpo.todo.domain.utils.Refs;
import de.lpo.todo.domain.utils.StringId;
import org.jetbrains.annotations.Nullable;
import org.jooq.generated.tables.records.TblBoardRecord;
import org.jooq.generated.tables.records.TblTodoRecord;
import org.jooq.generated.tables.records.TblUserRecord;

import java.util.List;

/**
 * A service that provides mapping utilities between {@link Board boards} and their table record representation.
 */
public class BoardMapper {
    private final TodoMapper todoMapper;
    private final UserMapper userMapper;

    public BoardMapper(TodoMapper todoMapper, UserMapper userMapper) {
        this.todoMapper = todoMapper;
        this.userMapper = userMapper;
    }

    /**
     * @param boardRecord the {@link TblBoardRecord board record} (required)
     * @param todoRecords the {@link TblTodoRecord task records} (optional; if null {@link Refs.Unloaded} is used)
     * @param ownerRecord the {@link TblUserRecord owner record} (optional; if null {@link Ref.Unloaded} is used)
     * @param memberRecords the {@link TblUserRecord member records} (optional; if null {@link Refs.Unloaded} is used)
     * @return a {@link Board board domain object}, filled with all the given data
     */
    public Board asDto(TblBoardRecord boardRecord,
                       @Nullable List<TblTodoRecord> todoRecords,
                       @Nullable TblUserRecord ownerRecord,
                       @Nullable List<TblUserRecord> memberRecords) {
        // Read data directly from board record
        final var id = boardRecord.getBoardId();
        final var title = boardRecord.getTitle();

        // Wrap todos into loaded references
        final Refs<Todo> todoRefs = todoRecords != null
                ? Refs.loaded(todoRecords.stream().filter(t -> t.getTodoId() != null).map(todoMapper::asDto).toList())
                : Refs.unloaded();

        // Wrap owner into a loaded reference
        final Ref<String, User> ownerRef = ownerRecord != null
                ? Ref.loaded(userMapper.asDto(ownerRecord))
                : Ref.unloaded(StringId.ofValue(boardRecord.getOwnerId()));

        // Wrap members into loaded references
        final Refs< User> memberRefs = memberRecords != null
                ? Refs.loaded(memberRecords.stream().filter(r -> r.getUserId() != null).map(userMapper::asDto).toList())
                : Refs.unloaded();

        // Create the resulting board object
        return new Board(IntId.ofValue(id), title, todoRefs, ownerRef, memberRefs);
    }
}
