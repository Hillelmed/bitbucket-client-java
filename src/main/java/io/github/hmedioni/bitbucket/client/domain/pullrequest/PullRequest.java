package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PullRequest implements LinksHolder {

    public int id;

    public int version;

    @Nullable
    public String title;

    @Nullable
    public String description;

    @Nullable
    public String state;

    public boolean open;

    public boolean closed;

    public long createdDate;

    public long updatedDate;

    @Nullable
    public Reference fromRef;

    @Nullable
    public Reference toRef;

    public boolean locked;

    @Nullable
    public Person author;

    public List<Person> reviewers;

    public List<Person> participants;

    @Nullable
    public Properties properties;
    @Nullable
    private Links links;

    @Override
    public Links links() {
        return links;
    }
}
