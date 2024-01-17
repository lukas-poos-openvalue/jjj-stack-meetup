package de.lpo.todo.domain.repository;

import de.lpo.todo.database.JooqService;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.Id;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record1;

import java.util.List;

import static org.jooq.generated.Tables.*;
import static org.jooq.impl.DSL.*;

/**
 * A repository to encapsulate database access in order to manage {@link User} objects.
 */
public class UserRepo {
    private final JooqService jooqService;
    private final UserMapper userMapper;

    public UserRepo(JooqService jooqService, UserMapper userMapper) {
        this.jooqService = jooqService;
        this.userMapper = userMapper;
    }

    /**
     * @param excludeUserId the user id to exclude
     * @return a list of all {@link User users} excluding the user with the given id
     */
    public List<User> findUsersExcluding(@NotNull Id<String> excludeUserId) {
        return jooqService.create()
                .selectFrom(TBL_USER)
                .where(TBL_USER.USER_ID.notEqual(excludeUserId.value()))
                .orderBy(TBL_USER.DISPLAY_NAME)
                .fetch(userMapper::asDto);
    }

    /**
     * @param userId the user id
     * @param boardId the board id
     * @return <code>true</code> if the user is either owner or member of the board
     */
    public boolean isUserOwnerOrMemberOfBoard(Id<String> userId, Id<Integer> boardId) {
        return jooqService.create()
                // select true if there are fitting records
                .select(field(count().gt(0)))

                // from board joining all members
                .from(TBL_BOARD)
                .leftJoin(TBL_BOARD_MEMBER)
                .on(TBL_BOARD_MEMBER.BOARD_ID.eq(TBL_BOARD.BOARD_ID))

                // where the board id fits and the user is either owner or member
                .where(TBL_BOARD.BOARD_ID.eq(boardId.value())
                        .and(TBL_BOARD.OWNER_ID.eq(userId.value()).or(TBL_BOARD_MEMBER.USER_ID.eq(userId.value())))
                )

                // execute and get the result
                .fetchSingle(Record1::value1);
    }
}
