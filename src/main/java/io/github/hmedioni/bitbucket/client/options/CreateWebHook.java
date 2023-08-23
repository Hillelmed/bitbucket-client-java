package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.repository.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


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

    public static CreateWebHook create(final String name,
                                       final List<WebHook.EventType> events,
                                       final String url,
                                       final boolean active,
                                       final WebHookConfiguration configuration) {
        return new CreateWebHook(name, events, configuration, url, active);
    }


}
