package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.branch.BranchConfiguration;
import io.github.hillelmed.bitbucket.client.domain.branch.BranchModelConfiguration;
import io.github.hillelmed.bitbucket.client.domain.branch.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Create branch model configuration.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBranchModelConfiguration {
    private BranchConfiguration development;
    @Nullable
    private BranchConfiguration production;
    private List<Type> types;

    /**
     * Instantiates a new Create branch model configuration.
     *
     * @param configuration the configuration
     */
    public CreateBranchModelConfiguration(final BranchModelConfiguration configuration) {
        this.development = configuration.getDevelopment();
        this.production = configuration.getProduction();
        this.types = configuration.getTypes();
    }

}
