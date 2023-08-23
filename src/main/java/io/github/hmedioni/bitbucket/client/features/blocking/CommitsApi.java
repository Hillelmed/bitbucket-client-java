package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.commit.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/api/latest/projects", accept = "application/json", contentType = "application/json")
public interface CommitsApi {


    @GetExchange("/{project}/repos/{repo}/commits/{commitId}")
    ResponseEntity<Commit> get(@PathVariable("project") String project,
                               @PathVariable("repo") String repo,
                               @PathVariable("commitId") String commitId,
                               @Nullable @RequestParam(required = false, name = "path") String path);


    @GetExchange("/{project}/repos/{repo}/commits/{commitId}/changes")
    ResponseEntity<ChangePage> listChanges(@PathVariable("project") String project,
                                           @PathVariable("repo") String repo,
                                           @PathVariable("commitId") String commitId,
                                           @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                           @Nullable @RequestParam(required = false, name = "start") Integer start);


    @GetExchange("/{project}/repos/{repo}/commits")
    ResponseEntity<CommitPage> list(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @Nullable @RequestParam(required = false, name = "withCounts") Boolean withCounts,
                                    @Nullable @RequestParam(required = false, name = "followRenames") Boolean followRenames,
                                    @Nullable @RequestParam(required = false, name = "ignoreMissing") Boolean ignoreMissing,
                                    @Nullable @RequestParam(required = false, name = "merges") String merges,
                                    @Nullable @RequestParam(required = false, name = "path") String path,
                                    @Nullable @RequestParam(required = false, name = "since") String since,
                                    @Nullable @RequestParam(required = false, name = "until") String until,
                                    @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                    @Nullable @RequestParam(required = false, name = "start") Integer start);
}
