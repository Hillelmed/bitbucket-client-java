package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchConfiguration {

    @Nullable
    private String refId;

    private boolean useDefault;

}
