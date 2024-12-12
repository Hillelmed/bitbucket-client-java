package io.github.hillelmed.bitbucket.client.domain.pullrequest;


import io.github.hillelmed.bitbucket.client.domain.common.Veto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * The type Merge status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeStatus {

    private boolean canMerge;

    private boolean conflicted;

    private List<Veto> vetoes;


}
