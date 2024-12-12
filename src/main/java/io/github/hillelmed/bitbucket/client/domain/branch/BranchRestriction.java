package io.github.hillelmed.bitbucket.client.domain.branch;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import io.github.hillelmed.bitbucket.client.domain.sshkey.AccessKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * The type Branch restriction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchRestriction {

    @Nullable
    private Long id;

    private BranchRestrictionEnumType type;

    private Matcher matcher;

    private List<User> users;

    private List<String> groups;

    @Nullable
    private List<AccessKey> accessKeys;

}
