package de.lpo.todo.auth;

import de.lpo.todo.auth.model.AuthUser;
import de.lpo.todo.auth.model.Role;
import de.lpo.todo.database.JooqService;
import org.jooq.generated.tables.records.TblUserRecord;

import java.util.Optional;

import static org.jooq.generated.Tables.TBL_USER;

/**
 * A repository to encapsulate database access in order to manage {@link AuthUser} objects.
 */
public class AuthUserRepo {

    private final JooqService jooqService;

    public AuthUserRepo(JooqService jooqService) {
        this.jooqService = jooqService;
    }

    /**
     * @param authUser the {@link AuthUser} to store. The record is updated when it already exists.
     */
    public void save(AuthUser authUser) {
        final var userRecord = mapToRecord(authUser);
        jooqService.create()
                .insertInto(TBL_USER)
                .set(userRecord)
                .onDuplicateKeyUpdate()
                .set(userRecord)
                .execute();
    }

    /**
     * @param userId the id of the {@link AuthUser}
     * @return A value for the {@link AuthUser} found, or an empty result
     */
    public Optional<AuthUser> findOneById(String userId) {
        return jooqService.create()
                .selectFrom(TBL_USER)
                .where(TBL_USER.USER_ID.eq(userId))
                .fetchOptional(this::mapToDto);
    }

    private TblUserRecord mapToRecord(AuthUser authUser) {
        return new TblUserRecord(authUser.userId(), authUser.userName(), authUser.email(), authUser.displayName(), authUser.role().name());
    }

    private AuthUser mapToDto(TblUserRecord record) {
        return new AuthUser(record.getUserId(), record.getName(), record.getEmail(), record.getDisplayName(), Role.valueOf(record.getRole()));
    }
}
