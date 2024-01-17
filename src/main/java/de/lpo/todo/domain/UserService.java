package de.lpo.todo.domain;

import de.lpo.todo.auth.AuthUserService;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.repository.UserMapper;
import de.lpo.todo.domain.repository.UserRepo;
import de.lpo.todo.domain.utils.Id;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service for working with {@link User users}.
 * A {@link User} is a domain view of the {@link de.lpo.todo.auth.model.AuthUser}.
 * Note that the access to users is primarily readonly.
 */
public class UserService {
    private final AuthUserService authUserService;
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserService(AuthUserService authUserService, UserRepo userRepo, UserMapper userMapper) {
        this.authUserService = authUserService;
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    /**
     * @return the {@link User} of the current session
     * @see AuthUserService#getFromContext(Context)
     */
    public User getFromContext(Context ctx) {
        return userMapper.asDto(authUserService.getFromContext(ctx));
    }

    /**
     * @param excludeUserId the user id to exclude from the result
     * @return a list of all users in the database, excluding the one with the given id
     */
    public List<User> findAllUsersExcluding(@NotNull Id<String> excludeUserId) {
        return userRepo.findUsersExcluding(excludeUserId);
    }

    /**
     * @param userId the user id
     * @param boardId the board id
     * @return <code>true</code>, when the given uses should have access to the given board.
     */
    public boolean canUserAccessBoard(@NotNull Id<String> userId, @NotNull Id<Integer> boardId) {
        return userRepo.isUserOwnerOrMemberOfBoard(userId, boardId);
    }
}
