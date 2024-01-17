package de.lpo.todo.domain.repository;

import de.lpo.todo.auth.model.AuthUser;
import de.lpo.todo.auth.model.Role;
import de.lpo.todo.domain.model.User;
import de.lpo.todo.domain.utils.StringId;
import org.jetbrains.annotations.NotNull;
import org.jooq.generated.tables.records.TblUserRecord;

/**
 * A service that provides mapping utilities between {@link User users} and their table record representation.
 */
public class UserMapper {
    /**
     *
     * @param userRecord the {@link TblUserRecord user record}
     * @return a {@link User user domain object}, filled with all the given data
     */
    public User asDto(@NotNull TblUserRecord userRecord) {
        return new User(
                StringId.ofValue(userRecord.getUserId()),
                userRecord.getDisplayName(),
                Role.valueOf(userRecord.getRole())
        );
    }

    /**
     * @param authUser the {@link AuthUser original auth user object}
     * @return a {@link User user domain object}, based on the original object
     */
    public User asDto(AuthUser authUser) {
        return new User(
                StringId.ofValue(authUser.userId()),
                authUser.displayName(),
                authUser.role()
        );
    }
}
