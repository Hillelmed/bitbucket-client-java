package io.github.hillelmed.bitbucket.client.domain.search;


import io.github.hillelmed.bitbucket.client.domain.project.Project;
import org.springframework.lang.Nullable;


/**
 * The type Search scope.
 */
public class SearchScope {

    /**
     * The Project.
     */
    @Nullable
    public Project project;

    /**
     * The Type.
     */
    @Nullable
    public String type;

    /**
     * The Query.
     */
    @Nullable
    public SearchQuery query;

}
