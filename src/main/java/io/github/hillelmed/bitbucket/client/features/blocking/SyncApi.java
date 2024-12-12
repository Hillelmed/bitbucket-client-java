package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.domain.sync.CreateSyncStatus;
import io.github.hillelmed.bitbucket.client.domain.sync.SyncState;
import io.github.hillelmed.bitbucket.client.domain.sync.SyncStatus;
import io.github.hillelmed.bitbucket.client.options.SyncOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;


/**
 * The interface Sync api.
 */
@HttpExchange(url = "/rest/sync/latest/projects", accept = "application/json", contentType = "application/json")
public interface SyncApi {


    /**
     * Enable response entity.
     *
     * @param project          the project
     * @param repo             the repo
     * @param createSyncStatus the create sync status
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}")
    ResponseEntity<SyncStatus> enable(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @RequestBody CreateSyncStatus createSyncStatus);


    /**
     * Status response entity.
     *
     * @param project     the project
     * @param repo        the repo
     * @param branchOrTag the branch or tag
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}")
    ResponseEntity<SyncStatus> status(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @Nullable @RequestParam(required = false, name = "at") String branchOrTag);


    /**
     * Synchronize response entity.
     *
     * @param project     the project
     * @param repo        the repo
     * @param syncOptions the sync options
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/synchronize")
    ResponseEntity<SyncState> synchronize(@PathVariable("project") String project,
                                          @PathVariable("repo") String repo,
                                          @RequestBody SyncOptions syncOptions);
}
