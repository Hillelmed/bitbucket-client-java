package io.github.hmedioni.bitbucket.client.domain.comment;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comments implements LinksHolder {

    public long createdDate;
    public long updatedDate;
    public List<Comments> comments;
    public List<Task> tasks;
    @Nullable
    public Anchor anchor;
    @Nullable
    public Link link;
    @Nullable
    public PermittedOperations permittedOperations;
    private Map<String, Object> properties;
    private int id;
    private int version;
    @Nullable
    private String text;
    @Nullable
    private Author author;

    @Nullable
    private Links links;

    @Nullable
    @Override
    public Links links() {
        return links;
    }
}
