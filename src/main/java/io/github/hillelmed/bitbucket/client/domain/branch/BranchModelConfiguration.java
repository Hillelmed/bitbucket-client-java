package io.github.hillelmed.bitbucket.client.domain.branch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Branch model configuration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchModelConfiguration {

    @Nullable
    private BranchConfiguration development;

    @Nullable
    private BranchConfiguration production;

    private List<Type> types;

}
