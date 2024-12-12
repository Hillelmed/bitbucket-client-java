package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.sshkey.AccessKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Create access key.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccessKey {

    private CreateKey key;

    private AccessKey.PermissionType permission;

}
