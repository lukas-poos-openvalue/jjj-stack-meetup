@import de.lpo.todo.domain.model.Board

@param Board board

@template.base.page-template(content = @`
    <nav class="container-fluid">
        <ul>
            <li>
                <a href="/app/boards" role="button" class="secondary outline">Back</a>
            </li>
        </ul>
        <ul>
            <li><strong>Todos</strong></li>
        </ul>
        <ul>
            <li><a style="visibility: hidden">hidden</a></li>
        </ul>
    </nav>

    <main class="container">
        <h1>Board ${board.title()}</h1>
        @template.todos.todo-list(boardId = board.id(), todos = board.todos().unwrap())
    </main>

    <style>
        nav {
            position: sticky;
            top: 0;
            background-color: var(--background-color);
            box-shadow: 0 1px 0 var(--accordion-border-color);
        }
    </style>
`)
