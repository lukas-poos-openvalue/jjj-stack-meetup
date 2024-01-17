package de.lpo.todo.dependencies;

import de.lpo.todo.auth.AuthUserRepo;
import de.lpo.todo.auth.AuthUserService;
import de.lpo.todo.auth.OidcSecurityService;
import de.lpo.todo.config.ConfigService;
import de.lpo.todo.database.DataSourceService;
import de.lpo.todo.database.DbMigrationService;
import de.lpo.todo.database.JooqService;
import de.lpo.todo.domain.*;
import de.lpo.todo.domain.repository.*;
import de.lpo.todo.web.handlers.IndexPageHandler;
import de.lpo.todo.web.JteService;
import de.lpo.todo.web.handlers.AdminPageHandler;
import de.lpo.todo.web.handlers.BoardsAddActionHandler;
import de.lpo.todo.web.handlers.BoardsAddModalHandler;
import de.lpo.todo.web.handlers.BoardsPageHandler;
import de.lpo.todo.web.handlers.BoardDeleteActionHandler;
import de.lpo.todo.web.handlers.BoardEditActionHandler;
import de.lpo.todo.web.handlers.BoardEditModalHandler;
import de.lpo.todo.web.handlers.TodosAddActionHandler;
import de.lpo.todo.web.handlers.TodosPageHandler;
import de.lpo.todo.web.handlers.TodoDeleteActionHandler;
import de.lpo.todo.web.handlers.TodoEditActionHandler;
import de.lpo.todo.web.handlers.TodoItemHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * [PoI] The dependency management for this application.
 * <ul>
 *     <li>All dependencies are managed as singletons</li>
 *     <li>All DI is done in this class</li>
 *     <li>All DI is done during compile time</li>
 *     <li>All dependencies are easily accessible when you have the application context</li>
 * </ul>
 */
public class ApplicationContext implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // Config
    public final ConfigService configService = new ConfigService();

    // Database
    public final DataSourceService dataSourceService = new DataSourceService(configService);
    public final DbMigrationService dbMigrationService = new DbMigrationService(dataSourceService);
    public final JooqService jooqService = new JooqService(configService, dataSourceService);

    // Auth
    public final AuthUserRepo authUserRepo = new AuthUserRepo(jooqService);
    public final AuthUserService authUserService = new AuthUserService(authUserRepo);
    public final OidcSecurityService oidcSecurityService = new OidcSecurityService(configService, authUserService);

    // Domain > Repository [Mappers]
    public final TodoMapper todoMapper = new TodoMapper();
    public final UserMapper userMapper = new UserMapper();
    public final BoardMapper boardMapper = new BoardMapper(todoMapper, userMapper);
    public final TodoRepo todoRepo = new TodoRepo(jooqService, todoMapper);
    public final BoardRepo boardRepo = new BoardRepo(jooqService, boardMapper);
    public final UserRepo userRepo = new UserRepo(jooqService, userMapper);
    public final TodoService todoService = new TodoService(todoRepo);
    public final BoardService boardService = new BoardService(boardRepo);
    public final UserService userService = new UserService(authUserService, userRepo, userMapper);
    public final BoardSecurityService boardSecurityService = new BoardSecurityService(userService);

    // Web
    public final JteService jteService = new JteService(configService);
    public final IndexPageHandler indexPageHandler = new IndexPageHandler(jteService);
    public final AdminPageHandler adminPageHandler = new AdminPageHandler(jteService, authUserService);
    public final BoardsPageHandler boardsPageHandler = new BoardsPageHandler(jteService, userService, boardService);
    public final BoardsAddModalHandler boardsAddModalHandler = new BoardsAddModalHandler(jteService, userService);
    public final BoardsAddActionHandler boardsAddActionHandler = new BoardsAddActionHandler(jteService, userService, boardService);
    public final BoardEditModalHandler boardEditModalHandler = new BoardEditModalHandler(jteService, userService, boardService);
    public final BoardEditActionHandler boardEditActionHandler = new BoardEditActionHandler(jteService, userService, boardService);
    public final BoardDeleteActionHandler boardDeleteActionHandler = new BoardDeleteActionHandler(boardService);
    public final TodosPageHandler todosPageHandler = new TodosPageHandler(jteService, boardService);
    public final TodosAddActionHandler todosAddActionHandler = new TodosAddActionHandler(jteService, todoService);
    public final TodoItemHandler todoItemHandler = new TodoItemHandler(jteService, todoService);
    public final TodoEditActionHandler todoEditActionHandler = new TodoEditActionHandler(jteService, todoService);
    public final TodoDeleteActionHandler todoDeleteActionHandler = new TodoDeleteActionHandler(todoService);

    @Override
    public void init() {
        // [PoI] Perform the initialization of the entire application context
        LOG.info("INIT APPLICATION CONTEXT - START\n");
        final var startTime = System.currentTimeMillis();
        configService.init();
        dataSourceService.init();
        dbMigrationService.updateDatabase();
        jteService.init();
        LOG.info("INIT APPLICATION CONTEXT - END (initialized in {}ms)\n", System.currentTimeMillis() - startTime);
    }
}
