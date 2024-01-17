@import de.lpo.todo.auth.model.Role
@import de.lpo.todo.domain.model.Board
@import de.lpo.todo.domain.model.User
@import java.util.Collection

@param User currentUser
@param Collection<Board> boards

<%-- [PoI] Include other templates easily --%>
@template.base.page-template(content = @`
    <nav class="container-fluid">
        <ul>
            <li>
                <%-- [PoI] Navigation via simple anchors --%>
                <a href="/api/logout"
                   onclick="return confirm('Are you sure you want to log out?')"
                   role="button"
                   class="secondary outline">
                    Logout
                </a>
            </li>
        </ul>
        <ul>
            <li><strong>Boards</strong></li>
        </ul>
        <ul>
            @if(currentUser.role() == Role.ROLE_ADMIN)
                <li><a href="/app/admin"
                       role="button"
                       class="outline">Admin</a></li>
            @else
                <li><a style="visibility: hidden">hidden</a></li>
            @endif
        </ul>
    </nav>

    <main class="container">
        <h1>Boards for ${currentUser.displayName()}</h1>
        @template.boards.board-list(currentUser = currentUser, boards = boards)
    </main>

    <%-- [PoI] Styles in body --%>
    <style>
        nav {
            position: sticky;
            top: 0;
            background-color: var(--background-color);
            box-shadow: 0 1px 0 var(--accordion-border-color);
        }
    </style>
`)
