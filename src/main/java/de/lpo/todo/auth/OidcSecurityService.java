package de.lpo.todo.auth;

import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import de.lpo.todo.auth.model.Role;
import de.lpo.todo.auth.workaround.CallbackHandler;
import de.lpo.todo.auth.workaround.LogoutHandler;
import de.lpo.todo.auth.workaround.SecurityHandler;
import de.lpo.todo.config.ConfigService;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.pac4j.core.config.Config;
import org.pac4j.core.matching.matcher.DefaultMatchers;
import org.pac4j.core.matching.matcher.csrf.CsrfTokenGeneratorMatcher;
import org.pac4j.core.matching.matcher.csrf.DefaultCsrfTokenGenerator;
import org.pac4j.oidc.client.KeycloakOidcClient;
import org.pac4j.oidc.config.KeycloakOidcConfiguration;

/**
 * Central service for managing web security.
 * The service utilizes 'pac4j' and OIDC for authentication and authorization.
 */
public class OidcSecurityService {
    public static final String CLIENT_NAME = KeycloakOidcClient.class.getSimpleName();

    private final ConfigService configService;
    private final AuthUserService authUserService;
    private Config securityConfig;

    public OidcSecurityService(ConfigService configService, AuthUserService authUserService) {
        this.configService = configService;
        this.authUserService = authUserService;
    }

    /**
     * [PoI] This handler that takes care of OIDC login callbacks and then updates the user database
     *
     * @param redirectUrl the url to redirect to after the login callback has been taken care of
     * @return the login callback handler
     */
    public Handler handleOidcLoginCallback(String redirectUrl) {
        final Handler loginCallback = new CallbackHandler(getSecurityConfig(), redirectUrl, true);
        return ctx -> {
            loginCallback.handle(ctx);
            final var authUser = authUserService.getFromContext(ctx);
            authUserService.save(authUser);
        };
    }

    /**
     * [PoI] This handler takes care of OIDC logouts. This logout destroys the entire session.
     *
     * @param redirectUrl the url to redirect to after the logout has been taken care of
     * @return the logout handler
     */
    public Handler handleOidcLogout(String redirectUrl) {
        final var handler = new LogoutHandler(getSecurityConfig(), redirectUrl);
        handler.centralLogout = true;
        handler.destroySession = true;
        return handler;
    }

    /**
     * [PoI] This handler protects any endpoint that it is applied to.
     * If the client is not authenticated yet, the authentication flow is started automatically.
     * After that, the default authenticators and matchers are applied, so the client has to provide
     * the CSRF token from the last response cookie if the request is anything other than a GET-request.
     *
     * @return the OIDC security handler
     */
    public Handler handleOidcSecurity() {
        return handleWorkaround(new SecurityHandler(getSecurityConfig(), CLIENT_NAME));
    }

    /**
     * [PoI] This handler protects assigned endpoints by looking at the authenticated user's role.
     * Requests from authenticated users that do not have the role are rejected.
     *
     * @return the admin role required handler
     */
    public Handler handleAdminRoleRequired() {
        return ctx -> {
            final var authUser = authUserService.getFromContext(ctx);
            if (authUser.role() != Role.ROLE_ADMIN) {
                throw new UnauthorizedResponse("Cannot access resource that is protected by admin role!");
            }
        };
    }

    private Config getSecurityConfig() {
        if (securityConfig == null) {
            final var config = configService.getConfig();

            // [PoI] We fill the security config with a client / authorizers / matchers
            final var securityConfig = new Config();

            // Client: KeycloakOidcClient
            final var client = new KeycloakOidcClient(
                    (KeycloakOidcConfiguration) new KeycloakOidcConfiguration()
                            .setBaseUri(config.authBaseUri())
                            .setRealm(config.authRealm())
                            .setClientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                            .setClientId(config.authClientId())
                            .setSecret(config.authSecret())
            ).setCallbackUrl(config.authCallbackUrl());
            securityConfig.addClient(client);

            // Matcher: Modify CSRF Token matcher, so that the client can access the CSRF-Token in the Cookies
            // (This matcher performs a side effect and sets a response cookie, but not in an accessible way)
            final var csrfTokenMatcher = new CsrfTokenGeneratorMatcher(new DefaultCsrfTokenGenerator());
            csrfTokenMatcher.setHttpOnly(false);
            securityConfig.addMatcher(DefaultMatchers.CSRF_TOKEN, csrfTokenMatcher);

            // Save our result
            this.securityConfig = securityConfig;
        }
        return securityConfig;
    }

    private Handler handleWorkaround(SecurityHandler securityHandler) {
        return ctx -> {
            // !!! [PoI]: Workaround !!!
            // Somehow, the entire body is cleansed from the Context.
            // This means that after successful authorization, things like form data is not retrievable.
            // By getting the request body, the data is somehow spared from removal :(
            ctx.body();

            // Delegate to the security handler
            securityHandler.handle(ctx);
        };
    }
}
