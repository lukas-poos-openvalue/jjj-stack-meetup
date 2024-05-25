package de.lpo.todo;

import de.lpo.todo.config.model.Profile;
import de.lpo.todo.dependencies.ApplicationContext;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TodoAppPlusMain {
    private static final ApplicationContext APP_CTX = new ApplicationContext();

    public static void main(String[] args) {
        // [PoI] Initialize application context
        APP_CTX.init();
        final var profile = APP_CTX.configService.getProfile();
        final var config = APP_CTX.configService.getConfig();

        final var app = Javalin.create(appConfig -> {
            // General config
            appConfig.useVirtualThreads = true;
            appConfig.http.brotliAndGzipCompression();

            // Serve assets as static files
            appConfig.staticFiles.add(staticFiles -> {
                // [PoI] Based on profile, serve assets from files or from resources
                final boolean isDev = profile == Profile.DEV;
                staticFiles.hostedPath = "/";
                staticFiles.location = isDev ? Location.EXTERNAL : Location.CLASSPATH;
                staticFiles.directory = isDev ? "src/main/resources/assets" : "/assets";
                staticFiles.precompress = !isDev;
            });

            // [PoI] Setup routes
            appConfig.router.apiBuilder(() -> {
                get(APP_CTX.indexPageHandler);

                path("api", () -> {
                    get("login", ctx -> ctx.redirect("/app"));
                    get("login/callback", APP_CTX.oidcSecurityService.handleOidcLoginCallback("/app"));
                    get("logout", APP_CTX.oidcSecurityService.handleOidcLogout("/"));
                });

                get("app", ctx -> ctx.redirect("/app/boards"));
                path("app", () -> {
                    before(APP_CTX.oidcSecurityService.handleOidcSecurity()); // [PoI] Everything below uses OIDC security

                    before("admin", APP_CTX.oidcSecurityService.handleAdminRoleRequired()); // [PoI] Admin path uses role security
                    get("admin", APP_CTX.adminPageHandler);

                    path("boards", () -> {
                        get(APP_CTX.boardsPageHandler);
                        post(APP_CTX.boardsAddActionHandler);

                        path("{board-id-hashed}", () -> {
                            before(APP_CTX.boardSecurityService.handleBoardAccess()); // [PoI] Board path uses access security
                            patch(APP_CTX.boardEditActionHandler);
                            delete(APP_CTX.boardDeleteActionHandler);

                            path("todos", () -> {
                                get(APP_CTX.todosPageHandler);
                                post(APP_CTX.todosAddActionHandler);

                                path("{todo-id-hashed}", () -> {
                                    get(APP_CTX.todoItemHandler);
                                    patch(APP_CTX.todoEditActionHandler);
                                    delete(APP_CTX.todoDeleteActionHandler);
                                });
                            });
                        });
                    });

                    path("modals", () -> {
                        path("/boards/add", () -> get(APP_CTX.boardsAddModalHandler));
                        path("/boards/{board-id-hashed}/edit", () -> {
                            before(APP_CTX.boardSecurityService.handleBoardAccess()); // [PoI] Board path uses access security
                            get(APP_CTX.boardEditModalHandler);
                        });
                    });
                });
            });
        });

        // Start the server with the configured port
        app.start(config.serverPort());
    }
}
