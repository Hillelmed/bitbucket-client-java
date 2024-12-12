package io.github.hillelmed.bitbucket.client.options;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Create project.
 */
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
