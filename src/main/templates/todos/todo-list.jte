@import java.util.Collection
@import de.lpo.todo.domain.model.Todo
@import de.lpo.todo.domain.utils.Id

@param Id<Integer> boardId
@param Collection<Todo> todos

!{
    var todosUrl = "/app/boards/" + boardId.hash() + "/todos";
}

<div id="todoList" class="todo-list">
    @for(var todo : todos)
        @template.todos.todo-item(todo = todo)
    @endfor
    <form hx-post="${todosUrl}"
          hx-target="this"
          hx-swap="beforebegin"
          hx-on--after-request="content.value = '';">
        <input id="content"
               name="content"
               placeholder="Add todo..."
               required />
    </form>
</div>

<style>
    .todo-list {
        min-width: 20em;
        display: flex;
        flex-flow: column;
        gap: 1em;
    }

    .todo-item {
        display: flex;
        align-items: center;
        gap: 1em;
    }

    .todo-item .content-read {
        flex-basis: 100%;
    }

    .todo-item .content-write {
        margin-bottom: 0 !important;
    }
</style>