@import de.lpo.todo.domain.model.Todo

@param Todo todo
@param boolean editing = false

!{
    var todoUrl = "/app/boards/" + todo.board().id().hash() + "/todos/" + todo.id().hash();
    var itemId = "todo" + todo.id().hash();
}

<div id="${itemId}" class="todo-item">
    <%-- [PoI] Pass values that normally are hard to aquire (unchecked checkboxes don't post any form data!) --%>
    <input type="checkbox"
           checked="${todo.completed()}"
           disabled="${editing}"
           hx-patch="${todoUrl}"
           hx-vals='{"completed": "${!todo.completed()}"}'
           hx-target="#${itemId}"
           hx-swap="outerHTML"/>

    <%-- [PoI] Support two states of todo items: editing true | false --%>
    @if(editing)
        <input class="content-write"
               name="content"
               value="${todo.content()}"
               autofocus
               hx-patch="${todoUrl}"
               hx-target="#${itemId}"
               hx-swap="outerHTML"/>

        <span role="button"
              class="secondary"
              hx-patch="${todoUrl}"
              hx-target="#${itemId}"
              hx-swap="outerHTML">
            <i class="gg-check"></i>
        </span>
        <span role="button"
              class="secondary"
              hx-get="${todoUrl}?edit=false"
              hx-target="#${itemId}"
              hx-swap="outerHTML">
            <i class="gg-close"></i>
        </span>
    @else
        <div class="content-read"
             hx-get="${todoUrl}?edit=true"
             hx-target="#${itemId}"
             hx-swap="outerHTML">
            ${todo.content()}
        </div>

        <span role="button"
              class="secondary"
              hx-delete="${todoUrl}"
              hx-target="#${itemId}"
              hx-swap="outerHTML">
            <i class="gg-trash"></i>
        </span>
    @endif
</div>