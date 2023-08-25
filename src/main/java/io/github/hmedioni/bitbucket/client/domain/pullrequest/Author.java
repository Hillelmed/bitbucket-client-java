package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import io.github.hmedioni.bitbucket.client.domain.comment.*;
import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;


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
