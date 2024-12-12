package io.github.hillelmed.bitbucket.client.domain.defaultreviewers;


import io.github.hillelmed.bitbucket.client.domain.branch.Matcher;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Condition.
 */
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
