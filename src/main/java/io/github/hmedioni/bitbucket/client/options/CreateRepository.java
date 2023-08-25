package io.github.hmedioni.bitbucket.client.options;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRepository {

    private String name;

    @Nullable
    private String description;

    private String scmId;

    private boolean forkable;

}
