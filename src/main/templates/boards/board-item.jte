@import de.lpo.todo.domain.model.Board
@import de.lpo.todo.domain.model.Todo
@import de.lpo.todo.domain.model.User
@import java.util.stream.Collectors

@param User currentUser
@param Board board

!{
    final var boardUrl = "/app/boards/" + board.id().hash();
    final var modalsBoardUrl = "/app/modals/boards/" + board.id().hash();

    final var ownerName = board.owner().unwrap().displayName();
    final var memberNames = board.members().unwrap().stream().map(User::displayName).collect(Collectors.joining(", "));
    final var todoCountCompleted = board.todos().unwrap().stream().filter(Todo::completed).count();
    final var todoCountTotal = board.todos().unwrap().size();

    final var itemId = "board" + board.id().hash();
    final var itemActionsId = "board" + board.id().hash() + "Actions";
}

<%-- [PoI] Each board item has a unique id that can be the target for HTMX swaps --%>
<div id="${itemId}" class="board-card">
    <a class="board-card-body" href="${boardUrl}/todos">
        <header>${board.title()}</header>
        <table>
            <tr>
                <td>Owner:</td>
                <td>${ownerName}</td>
            </tr>
            <tr>
                <td>Members:</td>
                <td>${memberNames}</td>
            </tr>
            <tr>
                <td>Todos:</td>
                @if(todoCountTotal != 0)
                    <td>${todoCountCompleted} / ${todoCountTotal} done</td>
                @else
                    <td>Nothing to do...</td>
                @endif
            </tr>
        </table>
    </a>

    @if(currentUser.id().equals(board.owner().id()))
        <details id="${itemActionsId}" class="board-card-footer" role="list">
            <summary>Actions</summary>
            <ul role="listbox">
                <li>
                    <%-- [PoI] Handle events with inline-Javascript --%>
                    <a href=""
                       hx-get="${modalsBoardUrl}/edit"
                       hx-target="#modal-target"
                       hx-on-click="${itemActionsId}.open = false;">
                        Edit
                    </a>
                </li>
                <li>
                    <%-- [PoI] Confirm dialog / delete by swap with empty --%>
                    <a href=""
                       hx-confirm="Are you sure you want to delete board '${board.title()}' permanently?"
                       hx-delete="${boardUrl}"
                       hx-target="#${itemId}"
                       hx-swap="outerHTML">
                        Delete
                    </a>
                </li>
            </ul>
        </details>
    @endif
</div>