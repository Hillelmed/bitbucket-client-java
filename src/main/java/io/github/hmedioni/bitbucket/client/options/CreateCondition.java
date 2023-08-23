package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.branch.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCondition {

    @Nullable
    public Long id;

    public Matcher sourceMatcher;

    public Matcher targetMatcher;

    public List<User> reviewers;

    public Long requiredApprovals;

}
