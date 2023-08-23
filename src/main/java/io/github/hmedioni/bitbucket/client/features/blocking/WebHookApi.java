package io.github.hmedioni.bitbucket.client.features.blocking;


import io.github.hmedioni.bitbucket.client.domain.repository.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/api/latest/projects", accept = "application/json", contentType = "application/json")
public interface WebHookApi {


    @GetExchange("/{project}/repos/{repo}/webhooks")
    ResponseEntity<WebHookPage> list(@PathVariable("project") String project,
                                     @PathVariable("repo") String repo,
                                     @Nullable @RequestParam(required = false, name = "start") Integer start,
                                     @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @GetExchange("/{project}/repos/{repo}/webhooks/{webHookKey}")
    ResponseEntity<WebHook> get(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("webHookKey") String webHookKey);


    @PostExchange("/{project}/repos/{repo}/webhooks")
    ResponseEntity<WebHook> create(@PathVariable("project") String project,
                                   @PathVariable("repo") String repo,
                                   @RequestBody CreateWebHook webHook);


    @PutExchange("/{project}/repos/{repo}/webhooks/{webHookKey}")
    ResponseEntity<WebHook> update(@PathVariable("project") String project,
                                   @PathVariable("repo") String repo,
                                   @PathVariable("webHookKey") String webHookKey,
                                   @RequestBody CreateWebHook webHook);


    @DeleteExchange("/{project}/repos/{repo}/webhooks/{webHookKey}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("webHookKey") String webHookKey);
}
