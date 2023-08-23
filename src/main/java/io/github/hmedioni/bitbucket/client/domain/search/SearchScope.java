package io.github.hmedioni.bitbucket.client.domain.search;


import io.github.hmedioni.bitbucket.client.domain.project.*;
import org.springframework.lang.*;


public class SearchScope {

    @Nullable
    public Project project;

    @Nullable
    public String type;

    @Nullable
    public SearchQuery query;

}
