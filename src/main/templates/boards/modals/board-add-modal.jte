@import java.util.Collection
@import de.lpo.todo.domain.model.User

@param Collection<User> userOptions

<dialog id="addBoardModal" open>
    <article>
        <header>
            Add Board
            <a class="close" hx-on-click="addBoardModal.remove();"></a>
        </header>

        <%-- [PoI] Insert before the add-button / handle HTMX-event "after-request" with removing the modal --%>
        <form id="addBoardForm"
              hx-post="/app/boards"
              hx-target="#addBoardBtn"
              hx-swap="beforebegin"
              hx-on--after-request="addBoardModal.remove();">
            <label for="title">Title*</label>
            <input id="title" name="title" placeholder="eg. shopping-list, homework, wishlist, ..." required autofocus />

            <label for="members">Members</label>
            <details id="members" role="list">
                <summary aria-haspopup="listbox">Select multiple members</summary>
                <ul role="listbox">
                    @for(var user : userOptions)
                    <li>
                        <label>
                            <input type="checkbox"
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
            <button type="submit" form="addBoardForm">Add board</button>
            <button type="button" class="secondary" hx-on-click="addBoardModal.remove();">
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


