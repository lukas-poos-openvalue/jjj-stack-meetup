package de.lpo.todo.web.handlers;

import de.lpo.todo.web.JteHandler;
import de.lpo.todo.web.JteService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

/**
 * Responds the HTML of the index page.
 */
public class IndexPageHandler extends JteHandler {
    private final JteService jteService;

    public IndexPageHandler(JteService jteService) {
        this.jteService = jteService;
    }

    @Override
    protected JteModel provideModel(@NotNull Context ctx) {
        return jteService.getTemplates().index();
    }
}
