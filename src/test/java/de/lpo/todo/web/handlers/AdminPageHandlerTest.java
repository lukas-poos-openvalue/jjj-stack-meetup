package de.lpo.todo.web.handlers;

import de.lpo.todo.auth.AuthUserService;
import de.lpo.todo.auth.model.AuthUser;
import de.lpo.todo.auth.model.Role;
import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class AdminPageHandlerTest {
    private final JteService jteService = mock();
    private final AuthUserService authUserService = mock();
    private final AdminPageHandler handler = new AdminPageHandler(jteService, authUserService);

    @Test
    void should_respond_admin_page() {
        // GIVEN
        // - Templates
        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        // - Auth user
        final var authUser = new AuthUser("a", "a", "a", "a", Role.ROLE_USER);
        when(authUserService.getFromContext(any())).thenReturn(authUser);

        // - Context
        final Context ctx = mock();

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).admin(authUser);
    }
}