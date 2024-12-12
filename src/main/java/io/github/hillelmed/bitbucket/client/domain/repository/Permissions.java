package io.github.hillelmed.bitbucket.client.domain.repository;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Permissions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permissions {

    @Nullable
    private User user;
    @Nullable
    private Group group;
    private PermissionsType permission;

    /**
     * The enum Permissions type.
     */
    public enum PermissionsType {
        /**
         * Repo admin permissions type.
         */
        REPO_ADMIN,
        /**
         * Repo write permissions type.
         */
        REPO_WRITE,
        /**
         * Repo read permissions type.
         */
        REPO_READ
    }

}
