package io.github.hmedioni.bitbucket.client.domain.pullrequest;

import lombok.*;
import org.springframework.lang.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Nullable
    private String name;

    @Nullable
    private String emailAddress;

    @Nullable
    private Integer id;

    @Nullable
    private String displayName;

    @Nullable
    private Boolean active;

    @Nullable
    private String slug;

    @Nullable
    private String type;

    @Nullable
    private String directoryName;

    @Nullable
    private Boolean deletable;

    @Nullable
    private Long lastAuthenticationTimestamp;

    @Nullable
    private Boolean mutableDetails;

    @Nullable
    private Boolean mutableGroups;

    public User(@Nullable final String name,
                @Nullable final String emailAddress,
                final int id,
                @Nullable final String displayName,
                final boolean active,
                @Nullable final String slug,
                @Nullable final String type) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.id = id;
        this.displayName = displayName;
        this.active = active;
        this.slug = slug;
        this.type = type;
    }

}
