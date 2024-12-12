package io.github.hillelmed.bitbucket.client.domain.sync;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * The type Create sync status.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSyncStatus {

    /**
     * The Enabled.
     */
    @Nullable
    public Boolean enabled;

}
