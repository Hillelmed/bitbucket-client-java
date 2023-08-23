package io.github.hmedioni.bitbucket.client.options;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncOptions {

    private String refId;
    private ACTION action;
    @Nullable
    private Context context;

    public static SyncOptions merge(@Nullable final String refId) {
        return new SyncOptions(refId, ACTION.MERGE, null);
    }

    public static SyncOptions discard(@Nullable final String refId) {
        return new SyncOptions(refId, ACTION.DISCARD, null);
    }

    public enum ACTION {
        MERGE,
        DISCARD
    }


}
