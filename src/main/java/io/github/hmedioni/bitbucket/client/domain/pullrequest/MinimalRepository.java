package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinimalRepository {

    public String slug;

    @Nullable
    public String name;

    public ProjectKey project;


}
