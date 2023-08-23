package io.github.hmedioni.bitbucket.client.domain.sshkey;


import io.github.hmedioni.bitbucket.client.domain.project.*;
import io.github.hmedioni.bitbucket.client.domain.repository.*;
import lombok.*;
import org.springframework.lang.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccessKey {

    @Nullable
    public PermissionType permission;
    @Nullable
    private Key key;
    @Nullable
    private Repository repository;
    @Nullable
    private Project project;

    public enum PermissionType {
        REPO_WRITE,
        REPO_READ,
        PROJECT_WRITE,
        PROJECT_READ
    }
}
