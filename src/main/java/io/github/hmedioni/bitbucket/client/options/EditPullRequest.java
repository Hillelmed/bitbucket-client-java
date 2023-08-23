package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


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
