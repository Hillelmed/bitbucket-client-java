package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchModel {

    @Nullable
    private Branch development;

    @Nullable
    private Branch production;

    private List<Type> types;

}
