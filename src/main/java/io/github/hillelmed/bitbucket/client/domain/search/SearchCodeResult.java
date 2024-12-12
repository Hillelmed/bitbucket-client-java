package io.github.hillelmed.bitbucket.client.domain.search;


import io.github.hillelmed.bitbucket.client.domain.repository.Repository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Search code result.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCodeResult {

    @Nullable
    private Repository repository;

    private List<List<SearchHitContext>> hitContexts;

    private List<SearchPathMatch> pathMatches;

    @Nullable
    private String file;

    @Nullable
    private Integer hitCount;

}
