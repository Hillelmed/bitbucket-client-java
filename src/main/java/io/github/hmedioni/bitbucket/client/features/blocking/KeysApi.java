package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.sshkey.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/keys/latest", accept = "application/json", contentType = "application/json")
public interface KeysApi {


    @GetExchange("/projects/{project}/repos/{repo}/ssh")
    ResponseEntity<AccessKeyPage> listByRepo(@PathVariable("project") String project,
                                             @PathVariable("repo") String repo,
                                             @Nullable @RequestParam(required = false, name = "start") Integer start,
                                             @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PostExchange("/projects/{project}/repos/{repo}/ssh")
    ResponseEntity<AccessKey> createForRepo(@PathVariable("project") String project,
                                            @PathVariable("repo") String repo,
                                            @RequestBody CreateAccessKey createAccessKey);


    @GetExchange("/projects/{project}/repos/{repo}/ssh/{id}")
    ResponseEntity<AccessKey> getForRepo(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("id") long id);


    @DeleteExchange("/projects/{project}/repos/{repo}/ssh/{id}")
    ResponseEntity<Void> deleteFromRepo(@PathVariable("project") String project,
                                        @PathVariable("repo") String repo,
                                        @PathVariable("id") long id);


    @GetExchange("/projects/{project}/ssh")
    ResponseEntity<AccessKeyPage> listByProject(@PathVariable("project") String project,
                                                @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PostExchange("/projects/{project}/ssh")
    ResponseEntity<AccessKey> createForProject(@PathVariable("project") String project,
                                               @RequestBody CreateAccessKey createAccessKey);


    @GetExchange("/projects/{project}/ssh/{id}")
    ResponseEntity<AccessKey> getForProject(@PathVariable("project") String project,
                                            @PathVariable("id") long id);


    @DeleteExchange("/projects/{project}/ssh/{id}")
    ResponseEntity<Void> deleteFromProject(@PathVariable("project") String project,
                                           @PathVariable("id") long id);

}
