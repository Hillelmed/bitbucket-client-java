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
    public Long id;

    public BranchRestrictionEnumType type;

    public Matcher matcher;

    public List<User> users;

    public List<String> groups;

    @Nullable
    public List<AccessKey> accessKeys;

}
