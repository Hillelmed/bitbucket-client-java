package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.common.Links;
import io.github.hillelmed.bitbucket.client.domain.common.Reference;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Create pull request.
 */
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

    /**
     * Instantiates a new Create pull request.
     *
     * @param title       the title
     * @param description the description
     * @param fromRef     the from ref
     * @param toRef       the to ref
     */
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
