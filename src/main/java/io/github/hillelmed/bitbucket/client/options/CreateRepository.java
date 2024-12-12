package io.github.hillelmed.bitbucket.client.options;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Create repository.
 */
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
