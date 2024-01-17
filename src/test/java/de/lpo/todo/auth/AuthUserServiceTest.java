package de.lpo.todo.auth;


import de.lpo.todo.auth.model.AuthUser;
import de.lpo.todo.auth.model.Role;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import org.pac4j.oidc.profile.keycloak.KeycloakOidcProfile;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthUserServiceTest {
    private final AuthUserRepo authUserRepo = mock(AuthUserRepo.class);
    private final AuthUserService authUserService = new AuthUserService(authUserRepo);

    @Test
    void should_read_from_context() {
        // GIVEN
        // - Profile
        final var userProfile = mock(KeycloakOidcProfile.class);
        when(userProfile.getId()).thenReturn("id");
        when(userProfile.getUsername()).thenReturn("name");
        when(userProfile.getEmail()).thenReturn("mail");
        when(userProfile.getDisplayName()).thenReturn("display");
        when(userProfile.getRoles()).thenReturn(Set.of("a", "b", Role.ROLE_ADMIN.name(), "c"));

        // - Context
        final var ctx = mock(Context.class);
        final var userProfiles = Map.of(OidcSecurityService.CLIENT_NAME, userProfile);
        when(ctx.sessionAttribute(AuthUserService.PROFILES_SESSION_ATTR)).thenReturn(userProfiles);

        // WHEN
        final var result = authUserService.getFromContext(ctx);

        // THEN
        assertThat(result)
                .extracting(AuthUser::userId, AuthUser::userName, AuthUser::email, AuthUser::displayName, AuthUser::role)
                .containsExactly("id", "name", "mail", "display", Role.ROLE_ADMIN);
    }

    @Test
    void should_save() {
        // GIVEN
        final var authUser = new AuthUser("id", "name", "mail", "display", Role.ROLE_USER);

        // WHEN
        authUserService.save(authUser);

        // THEN
        verify(authUserRepo).save(authUser);
    }

}