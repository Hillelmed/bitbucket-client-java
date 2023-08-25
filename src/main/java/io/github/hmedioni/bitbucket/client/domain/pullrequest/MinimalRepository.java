package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinimalRepository {

    private String slug;

    @Nullable
    private String name;

    private ProjectKey project;


}
