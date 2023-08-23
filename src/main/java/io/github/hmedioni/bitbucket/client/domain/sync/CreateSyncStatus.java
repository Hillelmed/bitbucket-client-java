package io.github.hmedioni.bitbucket.client.domain.sync;


import lombok.*;
import org.springframework.lang.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSyncStatus {

    @Nullable
    public Boolean enabled;

}
