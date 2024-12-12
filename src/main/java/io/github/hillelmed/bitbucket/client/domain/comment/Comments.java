package io.github.hillelmed.bitbucket.client.domain.comment;


import io.github.hillelmed.bitbucket.client.domain.common.Links;
import io.github.hillelmed.bitbucket.client.domain.common.LinksHolder;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;


/**
 * The type Comments.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comments implements LinksHolder {

    private long createdDate;
    private long updatedDate;
    private List<Comments> comments;
    private List<Task> tasks;
    @Nullable
    private Anchor anchor;
    @Nullable
    private Link link;
    @Nullable
    private PermittedOperations permittedOperations;
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
