package de.lpo.todo.auth;


import de.lpo.todo.auth.model.AuthUser;
import de.lpo.todo.auth.model.Role;
import de.lpo.utils.IntegrationTestDomain;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthUserRepoIT extends IntegrationTestDomain {

    @Test
    void should_save_and_find_by_id() {
        // GIVEN
        final var authUser = new AuthUser("id", "name", "mail", "display", Role.ROLE_USER);

        // WHEN
        appCtx.authUserRepo.save(authUser);
        final var result = appCtx.authUserRepo.findOneById(authUser.userId());

        // THEN
        assertThat(result).hasValue(authUser);
    }

}