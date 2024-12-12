package io.github.hillelmed.bitbucket.client.options;

import io.github.hillelmed.bitbucket.client.domain.branch.BranchRestriction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Create branch restriction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBranchRestriction {
    private List<BranchRestriction> branchRestrictions;
}