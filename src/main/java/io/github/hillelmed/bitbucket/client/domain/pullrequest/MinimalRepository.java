package io.github.hillelmed.bitbucket.client.domain.pullrequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Minimal repository.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinimalRepository {

    private String slug;

    @Nullable
    private String name;

    private ProjectKey project;


}
