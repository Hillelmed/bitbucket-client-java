package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Edit pull request.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditPullRequest {

    private int id;

    private int version;

    private String title;

    @Nullable
    private String description;

    // default to empty List if null
    @Nullable
    private List<Person> reviewers;
}
