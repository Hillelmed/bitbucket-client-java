package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePullRequest {

    private String title;

    @Nullable
    private String description;

    // set to "OPEN" for creating new PR's
    private String state;

    // set to TRUE for creating new PR's
    private boolean open;

    // set to FALSE for creating new PR's
    private boolean closed;

    private Reference fromRef;

    private Reference toRef;

    // set to FALSE for creating new PR's
    private boolean locked;

    // default to empty List if null
    @Nullable
    private List<Person> reviewers;

    @Nullable
    private Links links;

    public CreatePullRequest(String title, String description, Reference fromRef, Reference toRef) {
        this.title = title;
        this.description = description;
        this.state = "OPEN";
        this.open = true;
        this.closed = false;
        this.fromRef = fromRef;
        this.toRef = toRef;
        this.locked = false;
        this.reviewers = new ArrayList<>();
        this.links = new Links();
    }

}
