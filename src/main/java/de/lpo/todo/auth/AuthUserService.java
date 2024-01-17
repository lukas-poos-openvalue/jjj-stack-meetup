package de.lpo.todo.auth;

import de.lpo.todo.auth.model.AuthUser;
import de.lpo.todo.auth.model.Role;
import io.javalin.http.Context;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.oidc.profile.OidcProfile;

import java.util.Map;

/**
 * The service to manage {@link AuthUser} objects.
 */
public class AuthUserService {
    public static final String PROFILES_SESSION_ATTR = Pac4jConstants.USER_PROFILES;

    private final AuthUserRepo authUserRepo;

    public AuthUserService(AuthUserRepo authUserRepo) {
        this.authUserRepo = authUserRepo;
    }

    /**
     * Tries to find a current OIDC profile in the session attributes and provide a {@link AuthUser} for the found profile.
     * @param ctx the {@link Context} containing the session attributes
     * @return The value of the OIDC profile as a {@link AuthUser} or an empty response
     */
    public AuthUser getFromContext(Context ctx) {
        // Check if the session contains OIDC profiles
        final Map<String, OidcProfile> userProfiles = ctx.sessionAttribute(PROFILES_SESSION_ATTR);
        if (userProfiles == null) {
            throw new IllegalArgumentException("Session does not provide OIDC profiles!");
        }

        // The profiles are keyed by the OIDC client; find the correct one
        final var profile = userProfiles.get(OidcSecurityService.CLIENT_NAME);
        if (profile == null) {
            throw new IllegalArgumentException("Session does not provide OIDC profile for Keycloak client!");
        }

        // Map the profile data to an AuthUser
        return new AuthUser(
                profile.getId(),
                profile.getUsername(),
                profile.getEmail(),
                profile.getDisplayName(),
                Role.tryFirstValueOf(profile.getRoles()).orElseThrow()
        );
    }

    /**
     * @param authUser the {@link AuthUser} to store. The record is updated when it already exists.
     */
    public void save(AuthUser authUser) {
        authUserRepo.save(authUser);
    }
}
