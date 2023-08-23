package io.github.hmedioni.bitbucket.client.domain.search;


import lombok.*;
import org.springframework.lang.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {

    @Nullable
    private SearchScope scope;

    @Nullable
    private SearchCode code;

    @Nullable
    private SearchQuery query;

}
