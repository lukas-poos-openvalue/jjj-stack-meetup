@import java.util.Collection
@import de.lpo.todo.domain.model.Board
@import de.lpo.todo.domain.model.User

@param User currentUser
@param Collection<Board> boards

!{ final var modalsBoardsUrl = "/app/modals/boards"; }

<div id="boardList" class="board-list">
    @for(var board : boards)
        @template.boards.board-item(currentUser = currentUser, board = board)
    @endfor

    <%-- [PoI] Gets the "Add board"-modal and inserts it into the "modal-target" --%>
    <a id="addBoardBtn"
       class="board-card add-board-card"
       href=""
       hx-get="${modalsBoardsUrl}/add"
       hx-target="#modal-target">
        Add board...
    </a>

    <div id="modal-target"></div>
</div>

<style>
    .board-list {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(17em, 1fr));
        gap: 1em;
    }

    .board-card {
        display: grid;
        text-align: center;

        background-color: var(--card-background-color);
        box-shadow: var(--card-box-shadow);
        padding: 1em;
    }

    .board-card-body {
        display: flex;
        flex-direction: column;
        gap: 1em;
    }

    .board-card-body td {
        color: var(--h2-color);
    }

    .board-card-body:hover,
    .board-card-body:hover header,
    .board-card-body:hover td {
        text-decoration: none;
        color: var(--primary);
    }

    .board-card-footer {
        margin: 0;
    }

    .add-board-card {
        align-items: center;
    }
</style>