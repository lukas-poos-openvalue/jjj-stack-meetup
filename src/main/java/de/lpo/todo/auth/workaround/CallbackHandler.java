package de.lpo.todo.auth.workaround;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.pac4j.core.adapter.FrameworkAdapter;
import org.pac4j.core.config.Config;
import org.pac4j.jee.context.JEEFrameworkParameters;

import static org.pac4j.core.util.CommonHelper.assertNotNull;

public class CallbackHandler implements Handler {
    public Config config;
    public String defaultUrl;
    public Boolean renewSession;

    public CallbackHandler(Config config, String defaultUrl, Boolean renewSession) {
        assertNotNull("config", config);
        this.config = config;
        this.defaultUrl = defaultUrl;
        this.renewSession = renewSession;
    }

    @Override
    public void handle(@NotNull Context javalinCtx) {
        FrameworkAdapter.INSTANCE.applyDefaultSettingsIfUndefined(config);
        config.getCallbackLogic().perform(
                this.config,
                this.defaultUrl,
                this.renewSession,
                config.getClients().getClients().getFirst().getName(),
                new JEEFrameworkParameters(javalinCtx.req(), javalinCtx.res())
        );
    }
}