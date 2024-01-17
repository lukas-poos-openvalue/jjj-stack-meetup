package de.lpo.utils;

import de.lpo.todo.domain.utils.Id;
import gg.jte.generated.precompiled.Templates;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class WebTestUtils {
    public static Templates mockTemplates() {
        final Answer<JteModel> defaultAnswer = i -> {
            if (JteModel.class.equals(i.getMethod().getReturnType())) {
                return mock(JteModel.class);
            }
            return null;
        };
        return Mockito.mock(defaultAnswer);
    }

    public static void mockPathParamBoardId(Context ctx, Id<Integer> boardIdValue) {
        when(ctx.pathParam("{board-id-hashed}")).thenReturn(boardIdValue.hash());
    }

    public static void mockPathParamTodoId(Context ctx, Id<Integer> todoIdValue) {
        when(ctx.pathParam("{todo-id-hashed}")).thenReturn(todoIdValue.hash());
    }
}
