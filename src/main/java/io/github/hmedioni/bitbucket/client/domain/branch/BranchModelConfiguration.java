package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


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
