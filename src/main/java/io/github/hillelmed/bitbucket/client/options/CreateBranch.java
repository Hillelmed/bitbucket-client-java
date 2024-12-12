package io.github.hillelmed.bitbucket.client.options;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Create branch.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateBranch {

    private String name;

    private String startPoint;

    // defaults to value of name if null
    @Nullable
    private String message;

}
