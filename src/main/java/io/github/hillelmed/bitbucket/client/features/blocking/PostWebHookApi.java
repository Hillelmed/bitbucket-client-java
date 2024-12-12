package io.github.hillelmed.bitbucket.client.features.blocking;


import io.github.hillelmed.bitbucket.client.domain.postwebhooks.PostWebHook;
import io.github.hillelmed.bitbucket.client.options.CreatePostWebHook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

import java.util.List;

/**
 * The interface Post web hook api.
 */
@HttpExchange(url = "/rest/webhook/latest", accept = "application/json", contentType = "application/json")
public interface PostWebHookApi {


    /**
     * List response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @return the response entity
     */
    @GetExchange("/projects/{project}/repos/{repo}/webhook/configurations")
    ResponseEntity<List<PostWebHook>> list(@PathVariable("project") String project,
                                           @PathVariable("repo") String repo);


    /**
     * Update response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param postWebHookId the post web hook id
     * @param postWebHook   the post web hook
     * @return the response entity
     */
    @PutExchange("/projects/{project}/repos/{repo}/webhook/configurations/{postWebHookId}")
    ResponseEntity<PostWebHook> update(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("postWebHookId") String postWebHookId,
                                       @RequestBody CreatePostWebHook postWebHook);


    /**
     * Create response entity.
     *
     * @param project     the project
     * @param repo        the repo
     * @param postWebHook the post web hook
     * @return the response entity
     */
    @PostExchange("/projects/{project}/repos/{repo}/webhook/configurations")
    ResponseEntity<PostWebHook> create(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @RequestBody CreatePostWebHook postWebHook);


    /**
     * Delete response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param postWebHookId the post web hook id
     * @return the response entity
     */
    @DeleteExchange("/projects/{project}/repos/{repo}/webhook/configurations/{postWebHookId}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("postWebHookId") String postWebHookId);
}
