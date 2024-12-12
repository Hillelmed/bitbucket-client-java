package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.domain.tags.Tag;
import io.github.hillelmed.bitbucket.client.domain.tags.TagPage;
import io.github.hillelmed.bitbucket.client.options.CreateTag;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * The interface Tag api.
 */
@HttpExchange(accept = "application/json", contentType = "application/json")
public interface TagApi {


    /**
     * Create response entity.
     *
     * @param project   the project
     * @param repo      the repo
     * @param createTag the create tag
     * @return the response entity
     */
    @PostExchange("/rest/api/latest/projects/{project}/repos/{repo}/tags")
    ResponseEntity<Tag> create(@PathVariable("project") String project,
                               @PathVariable("repo") String repo,
                               @RequestBody CreateTag createTag);


    /**
     * Get response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param tag     the tag
     * @return the response entity
     */
    @GetExchange("/rest/api/latest/projects/{project}/repos/{repo}/tags/{tag}")
    ResponseEntity<Tag> get(@PathVariable("project") String project,
                            @PathVariable("repo") String repo,
                            @PathVariable("tag") String tag);


    /**
     * List response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param filterText the filter text
     * @param orderBy    the order by
     * @param start      the start
     * @param limit      the limit
     * @return the response entity
     */
    @GetExchange("/rest/api/latest/projects/{project}/repos/{repo}/tags")
    ResponseEntity<TagPage> list(@PathVariable("project") String project,
                                 @PathVariable("repo") String repo,
                                 @Nullable @RequestParam(required = false, name = "filterText") String filterText,
                                 @Nullable @RequestParam(required = false, name = "orderBy") String orderBy,
                                 @Nullable @RequestParam(required = false, name = "start") Integer start,
                                 @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Delete response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param tag     the tag
     * @return the response entity
     */
    @DeleteExchange("/rest/git/latest/projects/{project}/repos/{repo}/tags/{tag}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("tag") String tag);
}
