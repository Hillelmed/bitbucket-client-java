package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeStatus {

    private boolean canMerge;

    private boolean conflicted;

    private List<Veto> vetoes;


}
