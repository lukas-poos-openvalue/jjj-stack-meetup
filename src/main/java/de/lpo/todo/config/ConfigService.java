package de.lpo.todo.config;

import de.lpo.todo.config.model.Config;
import de.lpo.todo.config.model.Profile;
import de.lpo.todo.dependencies.Initializable;
import org.apache.commons.lang3.StringUtils;import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Properties;

/**
 * Service for providing the profile and the configuration that this app is started with.
 */
public class ConfigService implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String PROPERTIES_FILE_PATH = "config/application.properties";

    private Profile profile = null;
    private Config config = null;

    /**
     * @return The profile that the application has been started with
     * @throws IllegalStateException when the service hasn't been initialized yet
     */
    public Profile getProfile() {
        if (profile == null) {
            throw new IllegalStateException("Profile not initialized! Call ConfigService#init before!");
        }
        return profile;
    }

    /**
     * @return The config that the application has been started with
     * @throws IllegalStateException when the service hasn't been initialized yet
     */
    public Config getConfig() {
        if (config == null) {
            throw new IllegalStateException("Config not initialized! Call ConfigService#init before!");
        }
        return config;
    }

    /**
     * Reads the profile and the config:
     * <ul>
     *     <li>Profile: Read valid value with priority system properties > system environment.</li>
     *     <li>Config: Read properties with priority system properties > system environment > resources</li>
     * </ul>
     */
    @Override
    public void init() {
        if (profile != null && config != null) {
            LOG.warn("ConfigService was initialized before! Skipping init...");
            return;
        }

        // [PoI] Read profile from supported sources
        this.profile = readProfile();

        // [PoI] Read properties from supported sources
        final var properties = new Properties();
        properties.putAll(readPropertyFile());
        properties.putAll(readEnvironmentVariables());
        properties.putAll(readSystemProperties());

        // Set the finalized config
        this.config = new Config(properties);
        LOG.info("Config was initialized successfully!\nProfile: {}\nConfig: {}", profile, config.toDisplayString());
    }

    private Profile readProfile() {
        String profileStr = null;
        if (StringUtils.isNotBlank(System.getProperty(Config.KEY_ENV_PROFILE))) {
            profileStr = System.getProperty(Config.KEY_ENV_PROFILE);
        } else if (StringUtils.isNotBlank(System.getenv(Config.KEY_ENV_PROFILE))) {
            profileStr = System.getenv(Config.KEY_ENV_PROFILE);
        }
        return Profile.findValueOf(profileStr).orElse(Profile.NONE);
    }

    private Properties readPropertyFile() {
        try {
            final var propertiesInputStream = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE_PATH);
            if (propertiesInputStream == null) {
                return new Properties();
            }

            final var properties = new Properties();
            properties.load(propertiesInputStream);
            return properties;
        } catch (IOException e) {
            return new Properties();
        }
    }

    private Properties readEnvironmentVariables() {
        return convertMapToProperties(System.getenv());
    }

    private Properties readSystemProperties() {
        return System.getProperties();
    }

    private Properties convertMapToProperties(Map<String, String> stringMap) {
        final var result = new Properties();
        stringMap.forEach(result::setProperty);
        return result;
    }


}
