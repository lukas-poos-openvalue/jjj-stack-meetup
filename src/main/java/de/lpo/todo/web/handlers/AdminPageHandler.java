package de.lpo.todo.web.handlers;

import de.lpo.todo.auth.AuthUserService;
import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

/**
 * Responds the HTML of the admin page.
 */
public class AdminPageHandler extends JteHandler {
    private final JteService jteService;
    private final AuthUserService authUserService;

    public AdminPageHandler(JteService jteService, AuthUserService authUserService) {
        this.jteService = jteService;
        this.authUserService = authUserService;
    }

    @Override
    protected JteModel provideModel(@NotNull Context ctx) {
        final var currentAuthUser = authUserService.getFromContext(ctx);
        return jteService.getTemplates().admin(currentAuthUser);
    }
}
