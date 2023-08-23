package io.github.hmedioni.bitbucket.client.domain.repository;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import io.github.hmedioni.bitbucket.client.domain.project.*;
import lombok.*;
import org.springframework.lang.*;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Repository implements LinksHolder {

    @Nullable
    public String slug;

    public int id;

    @Nullable
    public String name;

    @Nullable
    public String description;

    @Nullable
    public String scmId;

    @Nullable
    public String state;

    @Nullable
    public String statusMessage;

    public boolean forkable;

    @Nullable
    public Repository origin;

    @Nullable
    public Project project;

    public boolean _public;

    private Links links;

    @Nullable
    @Override
    public Links links() {
        return links;
    }
}
