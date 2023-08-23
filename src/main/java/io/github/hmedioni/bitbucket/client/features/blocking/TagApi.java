package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.tags.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

@HttpExchange(accept = "application/json", contentType = "application/json")
public interface TagApi {


    @PostExchange("/rest/api/latest/projects/{project}/repos/{repo}/tags")
    ResponseEntity<Tag> create(@PathVariable("project") String project,
                               @PathVariable("repo") String repo,
                               @RequestBody CreateTag createTag);


    @GetExchange("/rest/api/latest/projects/{project}/repos/{repo}/tags/{tag}")
    ResponseEntity<Tag> get(@PathVariable("project") String project,
                            @PathVariable("repo") String repo,
                            @PathVariable("tag") String tag);


    @GetExchange("/rest/api/latest/projects/{project}/repos/{repo}/tags")
    ResponseEntity<TagPage> list(@PathVariable("project") String project,
                                 @PathVariable("repo") String repo,
                                 @Nullable @RequestParam(required = false, name = "filterText") String filterText,
                                 @Nullable @RequestParam(required = false, name = "orderBy") String orderBy,
                                 @Nullable @RequestParam(required = false, name = "start") Integer start,
                                 @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @DeleteExchange("/rest/git/latest/projects/{project}/repos/{repo}/tags/{tag}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("tag") String tag);
}
