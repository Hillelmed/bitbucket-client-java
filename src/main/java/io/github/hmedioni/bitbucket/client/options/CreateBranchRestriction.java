package io.github.hmedioni.bitbucket.client.options;

import io.github.hmedioni.bitbucket.client.domain.branch.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBranchRestriction {
    private List<BranchRestriction> branchRestrictions;
}