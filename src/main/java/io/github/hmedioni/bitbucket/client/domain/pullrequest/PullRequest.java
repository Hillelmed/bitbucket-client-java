package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


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
