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
    private Long id;

    private Matcher sourceMatcher;

    private Matcher targetMatcher;

    private List<User> reviewers;

    private Long requiredApprovals;

}
