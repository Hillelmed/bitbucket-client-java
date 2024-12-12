package io.github.hillelmed.bitbucket.client.domain.search;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * The type Search result.
 */
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
