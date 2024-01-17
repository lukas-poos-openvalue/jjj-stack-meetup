package de.lpo.todo.web;

import de.lpo.todo.config.ConfigService;
import de.lpo.todo.config.model.Profile;
import de.lpo.todo.dependencies.Initializable;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.generated.precompiled.DynamicTemplates;
import gg.jte.generated.precompiled.StaticTemplates;
import gg.jte.generated.precompiled.Templates;
import gg.jte.resolve.DirectoryCodeResolver;

import java.nio.file.Path;

/**
 * Service to give access to {@link Templates JTE templates}, which can be rendered to HTML in a type safe manner.
 * The template files are located in the 'templates' folder.
 */
public class JteService implements Initializable {
    private static final Path TEMPLATE_PATH_HMR = Path.of("src", "main", "templates");
    private static final Path OUTPUT_PATH_HMR = Path.of("target", "jte");

    private final ConfigService configService;
    private Templates templates;

    public JteService(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * @return the {@link Templates} for rendering HTML templates
     */
    public Templates getTemplates() {
        if (templates == null) {
            throw new IllegalStateException("JTE has not been initialized yet! Call JteService#init before!");
        }
        return templates;
    }

    /**
     * Initializes the {@link Templates}.
     * <p>
     * If the DEV profile is active, these templates are rendered dynamically,
     * so that changes to the template files are picked up while the application is running.
     * </p>
     * <p>
     * Otherwise, the templates are precompiled for optimized performance.
     * </p>
     */
    @Override
    public void init() {
        // Read the profile from the config
        final var profile = configService.getProfile();

        // [PoI] Set the templates to be either dynamic (for DEV) or static
        if (profile == Profile.DEV) {
            final var codeResolver = new DirectoryCodeResolver(TEMPLATE_PATH_HMR);
            final var templateEngine = TemplateEngine.create(codeResolver, OUTPUT_PATH_HMR, ContentType.Html);
            this.templates = new DynamicTemplates(templateEngine);
        } else {
            templates = new StaticTemplates();
        }
    }
}
