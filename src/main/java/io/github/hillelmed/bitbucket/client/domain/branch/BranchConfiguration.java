package io.github.hillelmed.bitbucket.client.domain.branch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Branch configuration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchConfiguration {

    @Nullable
    private String refId;

    private boolean useDefault;

}
