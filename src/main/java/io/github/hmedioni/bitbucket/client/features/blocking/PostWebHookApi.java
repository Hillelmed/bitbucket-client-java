package io.github.hmedioni.bitbucket.client.features.blocking;


import io.github.hmedioni.bitbucket.client.domain.postwebhooks.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.*;

@HttpExchange(url = "/rest/webhook/latest", accept = "application/json", contentType = "application/json")
public interface PostWebHookApi {


    @GetExchange("/projects/{project}/repos/{repo}/webhook/configurations")
    ResponseEntity<List<PostWebHook>> list(@PathVariable("project") String project,
                                           @PathVariable("repo") String repo);


    @PutExchange("/projects/{project}/repos/{repo}/webhook/configurations/{postWebHookId}")
    ResponseEntity<PostWebHook> update(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("postWebHookId") String postWebHookId,
                                       @RequestBody CreatePostWebHook postWebHook);


    @PostExchange("/projects/{project}/repos/{repo}/webhook/configurations")
    ResponseEntity<PostWebHook> create(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @RequestBody CreatePostWebHook postWebHook);


    @DeleteExchange("/projects/{project}/repos/{repo}/webhook/configurations/{postWebHookId}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("postWebHookId") String postWebHookId);
}
