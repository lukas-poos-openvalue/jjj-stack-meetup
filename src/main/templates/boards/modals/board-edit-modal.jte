@import java.util.Collection
@import de.lpo.todo.domain.model.Board
@import de.lpo.todo.domain.model.User

@param Board board
@param Collection<User> userOptions

!{
    var boardUrl = "/app/boards/" + board.id().hash();
    var boardId = "board" + board.id().hash();
    var memberIds = board.members().unwrap().stream().map(User::id).toList();
}

<dialog id="editBoardModal" open>
    <article>
        <header>
            Edit Board
            <a class="close" hx-on-click="editBoardModal.remove();"></a>
        </header>

        <form id="editBoardForm"
              hx-patch="${boardUrl}"
              hx-target="#${boardId}"
              hx-swap="outerHTML"
              hx-on--after-request="editBoardModal.remove();">
            <label for="title">Title*</label>
            <input id="title"
                   name="title"
                   value="${board.title()}"
                   placeholder="eg. shopping-list, homework, wishlist, ..."
                   required
                   autofocus/>

            <label for="permittedUsers">Members</label>
            <details id="permittedUsers" role="list">
                <summary aria-haspopup="listbox">Select multiple members</summary>
                <ul role="listbox">
                    @for(var user : userOptions)
                        <li>
                            <label>
                                <input type="checkbox"
                                       checked="${memberIds.contains(user.id())}"
                                       name="members"
                                       value="${user.id().hash()}">
                                ${user.displayName()}
                            </label>
                        </li>
                    @endfor
                </ul>
            </details>
        </form>

        <footer>
            <button type="submit" form="editBoardForm">Edit board</button>
            <button type="button" class="secondary" hx-on-click="editBoardModal.remove();">
                Cancel
            </button>
        </footer>
    </article>

    <style>
        article {
            width: max(50%, 40em);
        }
    </style>
</dialog>

