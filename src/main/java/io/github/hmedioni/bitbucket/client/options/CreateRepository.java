package io.github.hmedioni.bitbucket.client.options;


import lombok.*;
import org.springframework.lang.*;


@NoArgsConstructor
@AllArgsConstructor
public class CreateRepository {

    public String name;

    @Nullable
    public String description;

    public String scmId;

    public boolean forkable;

}
