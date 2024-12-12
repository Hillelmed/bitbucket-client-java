package io.github.hillelmed.bitbucket.client.domain.pullrequest;


import io.github.hillelmed.bitbucket.client.domain.comment.Link;
import io.github.hillelmed.bitbucket.client.domain.common.Links;
import io.github.hillelmed.bitbucket.client.domain.common.LinksHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Author.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author implements LinksHolder {

    private String name;

    @Nullable
    private String emailAddress;

    @Nullable
    private Integer id;

    @Nullable
    private String displayName;

    @Nullable
    private Boolean active;

    @Nullable
    private String slug;

    @Nullable
    private String type;

    @Nullable
    private Link link;

    @Nullable
    private Links links;

    @Nullable
    @Override
    public Links links() {
        return links;
    }
}
