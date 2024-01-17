package de.lpo.todo.web.handlers;


import de.lpo.todo.web.JteService;
import de.lpo.utils.WebTestUtils;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class IndexPageHandlerTest {
    private final JteService jteService = mock();
    private final IndexPageHandler handler = new IndexPageHandler(jteService);

    @Test
    void should_respond_index_page() {
        // GIVEN
        final Context ctx = mock();
        final var templates = WebTestUtils.mockTemplates();
        when(jteService.getTemplates()).thenReturn(templates);

        // WHEN
        handler.handle(ctx);

        // THEN
        verify(ctx).html(any());
        verify(templates).index();
    }
}