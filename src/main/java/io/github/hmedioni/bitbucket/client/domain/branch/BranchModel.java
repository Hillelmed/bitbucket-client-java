package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchModel {

    @Nullable
    public Branch development;

    @Nullable
    public Branch production;

    public List<Type> types;

}
