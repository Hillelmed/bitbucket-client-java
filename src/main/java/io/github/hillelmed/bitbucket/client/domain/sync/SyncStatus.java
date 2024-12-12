package io.github.hillelmed.bitbucket.client.domain.sync;


import io.github.hillelmed.bitbucket.client.domain.common.Reference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * The type Sync status.
 */
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
