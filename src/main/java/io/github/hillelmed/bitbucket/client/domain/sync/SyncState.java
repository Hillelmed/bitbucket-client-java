package io.github.hillelmed.bitbucket.client.domain.sync;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * The type Sync state.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncState {

    /**
     * The Id.
     */
    @Nullable
    public String id;

    /**
     * The Display id.
     */
    @Nullable
    public String displayId;

    /**
     * The State.
     */
    @Nullable
    public String state;

    /**
     * The Tag.
     */
    @Nullable
    public Boolean tag;
}
