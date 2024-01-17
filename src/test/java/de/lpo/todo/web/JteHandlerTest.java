package de.lpo.todo.web;

import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class JteHandlerTest {

    @Test
    void should_render_model_as_html() {
        // GIVEN
        final Context ctx = mock();
        final JteModel model = mock();
        when(model.render()).thenReturn("Hello World!");
        final var handler = mockJteHandler(model);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html("Hello World!");
    }

    @Test
    void should_ignore_null_model() {
        // GIVEN
        final Context ctx = mock();
        final var handler = mockJteHandler(null);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx, never()).html(any());
    }

    private JteHandler mockJteHandler(JteModel result) {
        return new JteHandler() {
            @Override
            protected JteModel provideModel(@NotNull Context ctx) {
                return result;
            }
        };
    }
}