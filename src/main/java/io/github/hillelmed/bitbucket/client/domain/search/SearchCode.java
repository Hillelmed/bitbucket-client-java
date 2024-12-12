package io.github.hillelmed.bitbucket.client.domain.search;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Search code.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCode {

    @Nullable
    private String category;

    private boolean isLastPage;

    @Nullable
    private Integer count;

    @Nullable
    private Integer start;

    @Nullable
    private Integer nextStart;

    @Nullable
    private List<SearchCodeResult> values;

}
