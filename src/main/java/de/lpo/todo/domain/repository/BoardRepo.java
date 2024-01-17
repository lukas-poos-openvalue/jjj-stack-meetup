package de.lpo.todo.domain.repository;

import de.lpo.todo.database.JooqService;
import de.lpo.todo.domain.model.Board;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.Id;
import de.lpo.todo.domain.utils.IntId;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record1;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.records.TblBoardMemberRecord;
import org.jooq.generated.tables.records.TblBoardRecord;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.jooq.generated.Tables.*;
import static org.jooq.impl.DSL.*;

/**
 * A repository to encapsulate database access in order to manage {@link Board} objects.
 */
public class BoardRepo {
    private final JooqService jooqService;
    private final BoardMapper boardMapper;

    public BoardRepo(JooqService jooqService, BoardMapper boardMapper) {
        this.jooqService = jooqService;
        this.boardMapper = boardMapper;
    }

    /**
     * Adds a new board and links to the members to the database.
     * @param title the title
     * @param ownerId the owner id (see {@link User#id()})
     * @param memberIds the member ids (see {@link User#id()})
     * @return the ID of the created {@link Board}
     */
    public Id<Integer> add(String title, Id<String> ownerId, @Nullable List<Id<String>> memberIds) {
        // Provide a value container for the resulting id
        final var boardIdWrapper = new AtomicInteger();

        // [PoI] Perform within transaction (synchronous)
        jooqService.create().transaction(trx -> {
            // Insert into the board table and remember the new id
            final var boardId = trx.dsl()
                    .insertInto(TBL_BOARD)
                    .set(TBL_BOARD.TITLE, title)
                    .set(TBL_BOARD.OWNER_ID, ownerId.value())
                    .returning(TBL_BOARD.BOARD_ID)
                    .fetchSingle(TblBoardRecord::getBoardId);
            boardIdWrapper.set(boardId);

            // Insert a link for each member id (if present)
            if (memberIds != null && !memberIds.isEmpty()) {
                trx.dsl()
                        .insertInto(Tables.TBL_BOARD_MEMBER)
                        .set(memberIds.stream()
                                .map(userId -> new TblBoardMemberRecord(boardId, userId.value()))
                                .toList()
                        )
                        .execute();
            }
        });

        // Return the obtained board id
        return IntId.ofValue(boardIdWrapper.get());
    }

    /**
     * Select a {@link Board} by given id and fetch all the related data of the board.
     * @param boardId the id of the {@link Board}
     * @return the value of the selected {@link Board} (including todos, owner, and members), or an empty result
     */
    public Optional<Board> findOneByIdFetchAll(Id<Integer> boardId) {
        // Create aliases for owner and member
        final var tblOwner = TBL_BOARD.tblUser().as("owner");
        final var tblMember = TBL_USER.as("member");

        return jooqService.create()
                .select(
                        // The board
                        TBL_BOARD,

                        // [PoI] All todos (with a subselect)
                        multiset(
                                selectFrom(TBL_TODO)
                                        .where(TBL_TODO.BOARD_ID.eq(TBL_BOARD.BOARD_ID))
                                        .orderBy(TBL_TODO.TODO_ID)
                        ),

                        // [PoI] The owner (with implicit join)
                        tblOwner,

                        // All members (with a subselect)
                        multiset(
                                select(tblMember)
                                        .from(tblMember)
                                        .join(TBL_BOARD_MEMBER)
                                        .on(TBL_BOARD_MEMBER.USER_ID.eq(tblMember.USER_ID))
                                        .and(TBL_BOARD_MEMBER.BOARD_ID.eq(boardId.value()))
                        )
                )
                .from(TBL_BOARD)
                .where(TBL_BOARD.BOARD_ID.eq(boardId.value()))

                // Project record to board
                .fetchOptional(r -> boardMapper.asDto(r.value1(), r.value2(), r.value3(), r.value4().map(Record1::value1)));
    }

    /**
     * Select all {@link Board boards} that relate to the given user.
     * @param user the user
     * @return A list of all {@link Board boards} that the given user is either owner or member of.
     */
    public List<Board> findAllForUserFetchAll(User user) {
        // Create aliases for owner and member
        final var tblOwner = TBL_USER.as("owner");
        final var tblMember = TBL_USER.as("member");

        return jooqService.create()
                .select(
                        // The board
                        TBL_BOARD,

                        // All todos (with a subselect)
                        multiset(
                                selectFrom(TBL_TODO)
                                        .where(TBL_TODO.BOARD_ID.eq(TBL_BOARD.BOARD_ID))
                                        .orderBy(TBL_TODO.TODO_ID)
                        ),

                        // The owner (with implicit join)
                        tblOwner,

                        // [PoI] All members (aggregation based on joins below)
                        multisetAgg(tblMember)
                )
                .from(TBL_BOARD)

                // Join owner
                .join(tblOwner)
                .on(tblOwner.USER_ID.eq(TBL_BOARD.OWNER_ID))

                // Join members
                .leftJoin(TBL_BOARD_MEMBER)
                .on(TBL_BOARD_MEMBER.BOARD_ID.eq(TBL_BOARD.BOARD_ID))
                .leftJoin(tblMember)
                .on(TBL_BOARD_MEMBER.USER_ID.eq(tblMember.USER_ID))

                // Filter by user beeing either owner or member
                .where(tblOwner.USER_ID.eq(user.id().value()))
                .or(tblMember.USER_ID.eq(user.id().value()))

                // Group by board and owner
                .groupBy(TBL_BOARD, tblOwner)
                .orderBy(TBL_BOARD.BOARD_ID)

                // Project each record to board
                .fetch(r -> boardMapper.asDto(r.value1(), r.value2(), r.value3(), r.value4().map(Record1::value1)));
    }

    /**
     * Updates a {@link Board} with the given data-
     * @param boardId the id of the {@link Board} to update
     * @param title the updated title value (optional)
     * @param memberIds the updated members (optional)
     */
    public void update(Id<Integer> boardId, @Nullable String title, @Nullable List<Id<String>> memberIds) {
        // Perform in transaction
        jooqService.create().transaction(trx -> {
            // Update the board if the title has changed
            if (title != null) {
                trx.dsl()
                        .update(TBL_BOARD)
                        .set(TBL_BOARD.TITLE, title)
                        .where(TBL_BOARD.BOARD_ID.eq(boardId.value()))
                        .execute();
            }

            // Update the members if they have changed
            if (memberIds != null) {
                trx.dsl()
                        .delete(TBL_BOARD_MEMBER)
                        .where(TBL_BOARD_MEMBER.BOARD_ID.eq(boardId.value()))
                        .execute();
                trx.dsl()
                        .insertInto(TBL_BOARD_MEMBER)
                        .set(memberIds.stream().map(userId -> new TblBoardMemberRecord(boardId.value(), userId.value())).toList())
                        .execute();
            }
        });
    }

    /**
     * Deletes a {@link Board} by the given id.
     * @param boardId the id of the {@link Board} to delete
     */
    public void deleteById(Id<Integer> boardId) {
        // Perform in transaction
        jooqService.create().transaction(trx -> {
            // Delete all todos of the board
            trx.dsl()
                    .delete(TBL_TODO)
                    .where(TBL_TODO.BOARD_ID.eq(boardId.value()))
                    .execute();

            // Delete all relations between members and the board
            trx.dsl()
                    .delete(TBL_BOARD_MEMBER)
                    .where(TBL_BOARD_MEMBER.BOARD_ID.eq(boardId.value()))
                    .execute();

            // Delete the board itself
            trx.dsl()
                    .delete(TBL_BOARD)
                    .where(TBL_BOARD.BOARD_ID.eq(boardId.value()))
                    .execute();
        });
    }
}
