package io.github.hmedioni.bitbucket.client.domain.project;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;


@NoArgsConstructor
@AllArgsConstructor
@Data

public class Project implements LinksHolder {

    @Nullable
    public String key;

    public int id;

    @Nullable
    public String name;

    @Nullable
    public String description;

    public boolean _public;

    @Nullable
    public String type;


    @Nullable
    private Links links;


    @Override
    public Links links() {
        return links;
    }
}
