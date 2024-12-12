package io.github.hillelmed.bitbucket.client.options;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Sync options.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncOptions {

    private String refId;
    private ACTION action;
    @Nullable
    private Context context;

    /**
     * Merge sync options.
     *
     * @param refId the ref id
     * @return the sync options
     */
    public static SyncOptions merge(@Nullable final String refId) {
        return new SyncOptions(refId, ACTION.MERGE, null);
    }

    /**
     * Discard sync options.
     *
     * @param refId the ref id
     * @return the sync options
     */
    public static SyncOptions discard(@Nullable final String refId) {
        return new SyncOptions(refId, ACTION.DISCARD, null);
    }

    /**
     * The enum Action.
     */
    public enum ACTION {
        /**
         * Merge action.
         */
        MERGE,
        /**
         * Discard action.
         */
        DISCARD
    }


}
