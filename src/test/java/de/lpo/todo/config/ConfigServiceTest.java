package de.lpo.todo.config;

import de.lpo.todo.config.model.Config;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigServiceTest {
    private final ConfigService configService = new ConfigService();

    @Test
    void should_read_test_configuration_properly() {
        // WHEN
        configService.init();
        final var result = configService.getConfig();

        // THEN
        assertThat(result)
                .extracting(Config::databaseDriver, Config::serverPort)
                .containsExactly("postgresql", 8080);
    }
}