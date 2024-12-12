package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.branch.Matcher;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Create condition.
 */
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
