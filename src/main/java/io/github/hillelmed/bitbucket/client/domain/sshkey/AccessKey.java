package io.github.hillelmed.bitbucket.client.domain.sshkey;


import io.github.hillelmed.bitbucket.client.domain.project.Project;
import io.github.hillelmed.bitbucket.client.domain.repository.Repository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Access key.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccessKey {

    /**
     * The Permission.
     */
    @Nullable
    public PermissionType permission;
    @Nullable
    private Key key;
    @Nullable
    private Repository repository;
    @Nullable
    private Project project;

    /**
     * The enum Permission type.
     */
    public enum PermissionType {
        /**
         * Repo write permission type.
         */
        REPO_WRITE,
        /**
         * Repo read permission type.
         */
        REPO_READ,
        /**
         * Project write permission type.
         */
        PROJECT_WRITE,
        /**
         * Project read permission type.
         */
        PROJECT_READ
    }
}
