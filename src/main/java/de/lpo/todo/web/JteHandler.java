package de.lpo.todo.web;

import de.lpo.todo.domain.utils.Id;
import de.lpo.todo.domain.utils.IntId;
import gg.jte.TemplateOutput;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class that makes its implementations return a {@link JteModel} (created by using {@link gg.jte.generated.precompiled.Templates}).
 */
public abstract class JteHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) {
        // [PoI] Response is filled with rendered model
        final var model = provideModel(ctx);
        if (model != null) {
            ctx.html(model.render());
        }
    }

    protected abstract @Nullable JteModel provideModel(@NotNull Context ctx);

    protected Id<Integer> getBoardIdFromPath(Context ctx) {
        final var boardId = IntId.ofHash(ctx.pathParam("{board-id-hashed}"));
        if (boardId == null) {
            throw new BadRequestResponse("URL does not contain board id!");
        }
        return boardId;
    }

    protected Id<Integer> getTodoIdFromPath(Context ctx) {
        final var todoId = IntId.ofHash(ctx.pathParam("{todo-id-hashed}"));
        if (todoId == null) {
            throw new BadRequestResponse("URL does not contain todo id!");
        }
        return todoId;
    }

    protected JteModel emptyModel() {
        return new JteModel() {
            @Override
            public void render(TemplateOutput output) {
                output.writeContent("");
            }

            @Override
            public void writeTo(TemplateOutput output) {
                output.writeContent("");
            }
        };
    }
}
