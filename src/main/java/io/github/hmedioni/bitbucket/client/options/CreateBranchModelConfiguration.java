package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.branch.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBranchModelConfiguration {
    private BranchConfiguration development;
    @Nullable
    private BranchConfiguration production;
    private List<Type> types;

    public CreateBranchModelConfiguration(final BranchModelConfiguration configuration) {
        this.development = configuration.getDevelopment();
        this.production = configuration.getProduction();
        this.types = configuration.getTypes();
    }

}
