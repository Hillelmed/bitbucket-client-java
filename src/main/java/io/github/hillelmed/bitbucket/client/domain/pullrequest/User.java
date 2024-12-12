package io.github.hillelmed.bitbucket.client.domain.pullrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * The type User.
 */
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

    /**
     * Instantiates a new User.
     *
     * @param name         the name
     * @param emailAddress the email address
     * @param id           the id
     * @param displayName  the display name
     * @param active       the active
     * @param slug         the slug
     * @param type         the type
     */
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
