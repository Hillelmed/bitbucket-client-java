package io.github.hillelmed.bitbucket.client.domain.project;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import io.github.hillelmed.bitbucket.client.domain.repository.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Project permissions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissions {

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
         * Project admin permissions type.
         */
        PROJECT_ADMIN,
        /**
         * Project write permissions type.
         */
        PROJECT_WRITE,
        /**
         * Project read permissions type.
         */
        PROJECT_READ,
        /**
         * Repo create permissions type.
         */
        REPO_CREATE
    }

}
