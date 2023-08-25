package io.github.hmedioni.bitbucket.client.domain.search;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


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
