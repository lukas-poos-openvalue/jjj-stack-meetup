package de.lpo.todo.config.model;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

/**
 * The supported configuration for this application.
 */
public record Config(@NotNull Integer serverPort,
                     @NotNull String databaseDriver,
                     @NotNull String databaseHost,
                     @NotNull Integer databasePort,
                     @NotNull String databaseName,
                     @NotNull String databaseUser,
                     @NotNull String databasePassword,
                     @NotNull String authBaseUri,
                     @NotNull String authRealm,
                     @NotNull String authClientId,
                     @NotNull String authSecret,
                     @NotNull String authCallbackUrl) {
    public static final String KEY_ENV_PROFILE = "env.profile";
    public static final String KEY_SERVER_PORT = "server.port";
    public static final String KEY_DATABASE_DRIVER = "database.driver";
    public static final String KEY_DATABASE_HOST = "database.host";
    public static final String KEY_DATABASE_PORT = "database.port";
    public static final String KEY_DATABASE_NAME = "database.name";
    public static final String KEY_DATABASE_USER = "database.user";
    public static final String KEY_DATABASE_PASSWORD = "database.password";
    public static final String KEY_AUTH_BASE_URI = "auth.baseUri";
    public static final String KEY_AUTH_REALM = "auth.realm";
    public static final String KEY_AUTH_CLIENT_ID = "auth.clientId";
    public static final String KEY_AUTH_SECRET = "auth.secret";
    public static final String KEY_AUTH_CALLBACK_URL = "auth.callbackUrl";

    private static int tryParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Config(Properties properties) {
        this(
                tryParseInt(properties.getProperty(KEY_SERVER_PORT, "")),
                properties.getProperty(KEY_DATABASE_DRIVER, ""),
                properties.getProperty(KEY_DATABASE_HOST, ""),
                tryParseInt(properties.getProperty(KEY_DATABASE_PORT, "")),
                properties.getProperty(KEY_DATABASE_NAME, ""),
                properties.getProperty(KEY_DATABASE_USER, ""),
                properties.getProperty(KEY_DATABASE_PASSWORD, ""),
                properties.getProperty(KEY_AUTH_BASE_URI, ""),
                properties.getProperty(KEY_AUTH_REALM, ""),
                properties.getProperty(KEY_AUTH_CLIENT_ID, ""),
                properties.getProperty(KEY_AUTH_SECRET, ""),
                properties.getProperty(KEY_AUTH_CALLBACK_URL, "")
        );
    }

    /**
     * @return Shorthand for the JDBC connection URL.
     */
    public String databaseUrl() {
        return "jdbc:%s://%s:%s/%s".formatted(databaseDriver, databaseHost, databasePort, databaseName);
    }

    /**
     * @return A pretty-printed version of {@link #toString()}. Removes passwords from the output.
     */
    public String toDisplayString() {
        var result  = toString();
        result = StringUtils.replace(result, "[", "[\n    ");
        result = StringUtils.replace(result, ", ", ",\n    ");
        result = StringUtils.replace(result, "]", "\n]");
        result = RegExUtils.replacePattern(result, "databasePassword=[^,]*", "databasePassword=****");
        result = RegExUtils.replacePattern(result, "authSecret=[^,]*", "authSecret=****");
        return result;
    }
}
