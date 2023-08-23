package io.github.hmedioni.bitbucket.client.domain.repository;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permissions {

    @Nullable
    private User user;
    @Nullable
    private Group group;
    private PermissionsType permission;

    public enum PermissionsType {
        REPO_ADMIN,
        REPO_WRITE,
        REPO_READ
    }

}
