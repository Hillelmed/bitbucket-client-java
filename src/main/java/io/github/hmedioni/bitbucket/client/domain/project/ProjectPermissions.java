package io.github.hmedioni.bitbucket.client.domain.project;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.domain.repository.*;
import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissions {

    @Nullable
    private User user;
    @Nullable
    private Group group;
    private PermissionsType permission;

    public enum PermissionsType {
        PROJECT_ADMIN,
        PROJECT_WRITE,
        PROJECT_READ,
        REPO_CREATE
    }

}
