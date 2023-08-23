package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.sshkey.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccessKey {

    private CreateKey key;

    private AccessKey.PermissionType permission;

}
