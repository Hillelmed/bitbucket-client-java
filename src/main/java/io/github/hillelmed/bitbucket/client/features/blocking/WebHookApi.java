package io.github.hillelmed.bitbucket.client.features.blocking;


import io.github.hillelmed.bitbucket.client.domain.repository.WebHook;
import io.github.hillelmed.bitbucket.client.domain.repository.WebHookPage;
import io.github.hillelmed.bitbucket.client.options.CreateWebHook;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;


/**
 * The interface Web hook api.
 */
@HttpExchange(url = "/rest/api/latest/projects", accept = "application/json", contentType = "application/json")
public interface WebHookApi {


    /**
     * List response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/webhooks")
    ResponseEntity<WebHookPage> list(@PathVariable("project") String project,
                                     @PathVariable("repo") String repo,
                                     @Nullable @RequestParam(required = false, name = "start") Integer start,
                                     @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Get response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param webHookKey the web hook key
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/webhooks/{webHookKey}")
    ResponseEntity<WebHook> get(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("webHookKey") String webHookKey);


    /**
     * Create response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param webHook the web hook
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/webhooks")
    ResponseEntity<WebHook> create(@PathVariable("project") String project,
                                   @PathVariable("repo") String repo,
                                   @RequestBody CreateWebHook webHook);


    /**
     * Update response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param webHookKey the web hook key
     * @param webHook    the web hook
     * @return the response entity
     */
    @PutExchange("/{project}/repos/{repo}/webhooks/{webHookKey}")
    ResponseEntity<WebHook> update(@PathVariable("project") String project,
                                   @PathVariable("repo") String repo,
                                   @PathVariable("webHookKey") String webHookKey,
                                   @RequestBody CreateWebHook webHook);


    /**
     * Delete response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param webHookKey the web hook key
     * @return the response entity
     */
    @DeleteExchange("/{project}/repos/{repo}/webhooks/{webHookKey}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("webHookKey") String webHookKey);
}
