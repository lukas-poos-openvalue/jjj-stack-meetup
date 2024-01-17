package de.lpo.todo.auth;


import de.lpo.todo.config.ConfigService;
import de.lpo.todo.config.model.Config;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OidcSecurityServiceTest {

    private final ConfigService configService = mock(ConfigService.class);
    private final AuthUserService authUserService = mock(AuthUserService.class);

    private final OidcSecurityService oidcSecurityService = new OidcSecurityService(configService, authUserService);

    private static Config oneConfig() {
        return new Config(
            1234, "", "", 2345, "", "", "",
                "authBaseUri",
                "authRealm",
                "authClientId",
                "authSecret",
                "authCallbackUrl"
        );
    }

    @Test
    void should_get_handlers() {
        // GIVEN
        final var config = oneConfig();
        when(configService.getConfig()).thenReturn(config);

        // WHEN
        final var loginCallbackHandler = oidcSecurityService.handleOidcLoginCallback("");
        final var generalProtectionHandler = oidcSecurityService.handleOidcSecurity();
        final var adminRoleHandler = oidcSecurityService.handleAdminRoleRequired();
        final var logoutHandler = oidcSecurityService.handleOidcLogout("");

        // THEN
        assertThat(loginCallbackHandler).isNotNull();
        assertThat(generalProtectionHandler).isNotNull();
        assertThat(adminRoleHandler).isNotNull();
        assertThat(logoutHandler).isNotNull();
    }
}