package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import io.github.hmedioni.bitbucket.client.domain.comment.*;
import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author implements LinksHolder {

    public String name;

    @Nullable
    public String emailAddress;

    @Nullable
    public Integer id;

    @Nullable
    public String displayName;

    @Nullable
    public Boolean active;

    @Nullable
    public String slug;

    @Nullable
    public String type;

    @Nullable
    public Link link;

    @Nullable
    private Links links;

    @Nullable
    @Override
    public Links links() {
        return links;
    }
}
