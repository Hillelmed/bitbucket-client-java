package io.github.hmedioni.bitbucket.client.domain.search;


import io.github.hmedioni.bitbucket.client.domain.repository.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


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
