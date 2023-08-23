package io.github.hmedioni.bitbucket.client.domain.search;


import org.springframework.lang.*;

import java.util.*;


public class SearchCode {

    @Nullable
    public String category;

    public boolean isLastPage;

    @Nullable
    public Integer count;

    @Nullable
    public Integer start;

    @Nullable
    public Integer nextStart;

    @Nullable
    public List<SearchCodeResult> values;

}
