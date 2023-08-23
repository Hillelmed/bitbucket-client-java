package io.github.hmedioni.bitbucket.client.domain.sync;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SyncStatus {

    @Nullable
    private Boolean available;

    @Nullable
    private Boolean enabled;

    @Nullable
    private Long lastSync;

    private List<Reference> aheadRefs;

    private List<Reference> divergedRefs;

    private List<Reference> orphanedRefs;

}
