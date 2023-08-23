package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.sync.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/sync/latest/projects", accept = "application/json", contentType = "application/json")
public interface SyncApi {


    @PostExchange("/{project}/repos/{repo}")
    ResponseEntity<SyncStatus> enable(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @RequestBody CreateSyncStatus createSyncStatus);


    @GetExchange("/{project}/repos/{repo}")
    ResponseEntity<SyncStatus> status(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @Nullable @RequestParam(required = false, name = "at") String branchOrTag);


    @PostExchange("/{project}/repos/{repo}/synchronize")
    ResponseEntity<SyncState> synchronize(@PathVariable("project") String project,
                                          @PathVariable("repo") String repo,
                                          @RequestBody SyncOptions syncOptions);
}
