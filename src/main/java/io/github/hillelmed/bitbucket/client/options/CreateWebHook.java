package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.repository.WebHook;
import io.github.hillelmed.bitbucket.client.domain.repository.WebHookConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Create web hook.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWebHook {

    private String name;

    private List<WebHook.EventType> events;

    @Nullable
    private WebHookConfiguration configuration;

    private String url;

    private boolean active;

    /**
     * Create create web hook.
     *
     * @param name          the name
     * @param events        the events
     * @param url           the url
     * @param active        the active
     * @param configuration the configuration
     * @return the create web hook
     */
    public static CreateWebHook create(final String name,
                                       final List<WebHook.EventType> events,
                                       final String url,
                                       final boolean active,
                                       final WebHookConfiguration configuration) {
        return new CreateWebHook(name, events, configuration, url, active);
    }


}
