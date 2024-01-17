package de.lpo.todo.web;


import de.lpo.todo.config.ConfigService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

class JteServiceTest {
    private final ConfigService configService = mock(ConfigService.class);


    @Test
    void should_throw_if_not_initialized() {
        // GIVEN
        final var jteService = new JteService(configService);

        // WHEN
        final var result = catchThrowable(jteService::getTemplates);

        // THEN
        assertThat(result)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("init");
    }

    @Test
    void should_initialize_properly() {
        // GIVEN
        final var jteService = new JteService(configService);

        // WHEN
        jteService.init();
        final var result = jteService.getTemplates();

        // THEN
        assertThat(result).isNotNull();
    }
}