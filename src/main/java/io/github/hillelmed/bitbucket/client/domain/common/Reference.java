package io.github.hillelmed.bitbucket.client.domain.common;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.MinimalRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import static io.github.hillelmed.bitbucket.client.BitbucketConstants.REFS_HEADS_MASTER;


/**
 * The type Reference.
 */
@Data
@NoArgsConstructor
public class Reference {

    /**
     * The Id.
     */
// default to 'refs/heads/master' if null
    @Nullable
    public String id;

    /**
     * The Repository.
     */
    @Nullable
    public MinimalRepository repository;

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

    /**
     * The Display id.
     */
    @Nullable
    public String displayId;

    /**
     * The Latest commit.
     */
    @Nullable
    public String latestCommit;

    /**
     * Instantiates a new Reference.
     *
     * @param id         the id
     * @param repository the repository
     */
    public Reference(@Nullable String id, @Nullable MinimalRepository repository) {
        this.id = id != null ? id : REFS_HEADS_MASTER;
        this.repository = repository;
    }

    /**
     * Instantiates a new Reference.
     *
     * @param id            the id
     * @param repository    the repository
     * @param branchToMerge the branch to merge
     */
    public Reference(@Nullable String id, @Nullable MinimalRepository repository, @Nullable String branchToMerge) {
        this.id = id != null ? id : REFS_HEADS_MASTER;
        this.repository = repository;
        this.displayId = branchToMerge;
    }

    /**
     * Instantiates a new Reference.
     *
     * @param id           the id
     * @param repository   the repository
     * @param state        the state
     * @param tag          the tag
     * @param displayId    the display id
     * @param latestCommit the latest commit
     */
    public Reference(@Nullable String id, @Nullable MinimalRepository repository, @Nullable String state, @Nullable Boolean tag, @Nullable String displayId, @Nullable String latestCommit) {
        this.id = id != null ? id : REFS_HEADS_MASTER;
        this.repository = repository;
        this.state = state;
        this.tag = tag;
        this.displayId = displayId;
        this.latestCommit = latestCommit;
    }
}
