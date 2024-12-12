package io.github.hillelmed.bitbucket.client.features.blocking;


import io.github.hillelmed.bitbucket.client.domain.repository.Hook;
import io.github.hillelmed.bitbucket.client.domain.repository.HookPage;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

/**
 * The interface Hook api.
 */
@HttpExchange(url = "/rest/api/latest/projects", accept = "application/json", contentType = "application/json")
public interface HookApi {


    /**
     * List response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/settings/hooks")
    ResponseEntity<HookPage> list(@PathVariable("project") String project,
                                  @PathVariable("repo") String repo,
                                  @Nullable @RequestParam(required = false, name = "start") Integer start,
                                  @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Get response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param hookKey the hook key
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/settings/hooks/{hookKey}")
    ResponseEntity<Hook> get(@PathVariable("project") String project,
                             @PathVariable("repo") String repo,
                             @PathVariable("hookKey") String hookKey);


    /**
     * Enable response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param hookKey the hook key
     * @return the response entity
     */
    @PutExchange("/{project}/repos/{repo}/settings/hooks/{hookKey}/enabled")
    ResponseEntity<Hook> enable(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("hookKey") String hookKey);


    /**
     * Disable response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param hookKey the hook key
     * @return the response entity
     */
    @DeleteExchange("/{project}/repos/{repo}/settings/hooks/{hookKey}/enabled")
    ResponseEntity<Hook> disable(@PathVariable("project") String project,
                                 @PathVariable("repo") String repo,
                                 @PathVariable("hookKey") String hookKey);
}
