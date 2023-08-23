package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.branch.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBranchModelConfiguration {
    public BranchConfiguration development;
    @Nullable
    public BranchConfiguration production;
    public List<Type> types;

    public CreateBranchModelConfiguration(final BranchModelConfiguration configuration) {
        this.development = configuration.getDevelopment();
        this.production = configuration.getProduction();
        this.types = configuration.getTypes();
    }

}
