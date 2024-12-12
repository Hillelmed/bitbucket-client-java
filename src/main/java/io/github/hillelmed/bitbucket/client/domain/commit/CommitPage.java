package io.github.hillelmed.bitbucket.client.domain.commit;


import io.github.hillelmed.bitbucket.client.domain.common.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;


/**
 * The type Commit page.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommitPage extends Page<Commit> {

    @Nullable
    private Integer authorCount;

    @Nullable
    private Integer totalCount;

}
