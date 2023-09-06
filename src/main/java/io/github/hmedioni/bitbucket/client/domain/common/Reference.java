package io.github.hmedioni.bitbucket.client.domain.common;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import static io.github.hmedioni.bitbucket.client.BitbucketConstants.*;


@Data
@NoArgsConstructor
public class Reference {

    // default to 'refs/heads/master' if null
    @Nullable
    public String id;

    @Nullable
    public MinimalRepository repository;

    @Nullable
    public String state;

    @Nullable
    public Boolean tag;

    @Nullable
    public String displayId;

    @Nullable
    public String latestCommit;

    public Reference(@Nullable String id, @Nullable MinimalRepository repository) {
        this.id = id != null ? id : REFS_HEADS_MASTER;
        this.repository = repository;
    }

    public Reference(@Nullable String id, @Nullable MinimalRepository repository, @Nullable String branchToMerge) {
        this.id = id != null ? id : REFS_HEADS_MASTER;
        this.repository = repository;
        this.displayId = branchToMerge;
    }

    public Reference(@Nullable String id, @Nullable MinimalRepository repository, @Nullable String state, @Nullable Boolean tag, @Nullable String displayId, @Nullable String latestCommit) {
        this.id = id != null ? id : REFS_HEADS_MASTER;
        this.repository = repository;
        this.state = state;
        this.tag = tag;
        this.displayId = displayId;
        this.latestCommit = latestCommit;
    }
}
