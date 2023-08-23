package io.github.hmedioni.bitbucket.client.options;


import lombok.*;
import org.springframework.lang.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateProject {

    private String key;

    // defaults to value of key if null
    @Nullable
    private String name;

    @Nullable
    private String description;

    @Nullable
    private String avatar;

}
