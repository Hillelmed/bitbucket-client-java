package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchModelConfiguration {

    @Nullable
    public BranchConfiguration development;

    @Nullable
    public BranchConfiguration production;

    public List<Type> types;

}
