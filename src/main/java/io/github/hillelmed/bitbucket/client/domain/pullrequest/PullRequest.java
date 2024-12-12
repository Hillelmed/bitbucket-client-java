package io.github.hillelmed.bitbucket.client.domain.pullrequest;


import io.github.hillelmed.bitbucket.client.domain.common.Links;
import io.github.hillelmed.bitbucket.client.domain.common.LinksHolder;
import io.github.hillelmed.bitbucket.client.domain.common.Reference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Pull request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PullRequest implements LinksHolder {

    private int id;

    private int version;

    @Nullable
    private String title;

    @Nullable
    private String description;

    @Nullable
    private String state;

    private boolean open;

    private boolean closed;

    private long createdDate;

    private long updatedDate;

    @Nullable
    private Reference fromRef;

    @Nullable
    private Reference toRef;

    private boolean locked;

    @Nullable
    private Person author;

    private List<Person> reviewers;

    private List<Person> participants;

    @Nullable
    private Properties properties;
    @Nullable
    private Links links;

    @Override
    public Links links() {
        return links;
    }
}
