package io.github.hmedioni.bitbucket.client.domain.defaultreviewers;


import io.github.hmedioni.bitbucket.client.domain.branch.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Condition {

    @Nullable
    private Long id;

    @Nullable
    private Scope scope;

    @Nullable
    private Matcher sourceRefMatcher;

    @Nullable
    private Matcher targetRefMatcher;

    @Nullable
    private List<User> reviewers;

    @Nullable
    private Long requiredApprovals;


}
