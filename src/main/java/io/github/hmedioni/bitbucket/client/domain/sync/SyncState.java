package io.github.hmedioni.bitbucket.client.domain.sync;


import lombok.*;
import org.springframework.lang.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncState {

    @Nullable
    public String id;

    @Nullable
    public String displayId;

    @Nullable
    public String state;

    @Nullable
    public Boolean tag;
}
