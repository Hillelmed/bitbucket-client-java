package io.github.hmedioni.bitbucket.client.domain.branch;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.domain.sshkey.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;

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
