package io.github.hmedioni.bitbucket.client.domain.commit;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;


@EqualsAndHashCode(callSuper = true)
@Data
public class CommitPage extends Page<Commit> {

    @Nullable
    private Integer authorCount;

    @Nullable
    private Integer totalCount;

}
