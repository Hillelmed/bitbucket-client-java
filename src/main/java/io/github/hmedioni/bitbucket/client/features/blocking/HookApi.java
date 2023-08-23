package io.github.hmedioni.bitbucket.client.features.blocking;


import io.github.hmedioni.bitbucket.client.domain.repository.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

@HttpExchange(url = "/rest/api/latest/projects", accept = "application/json", contentType = "application/json")
public interface HookApi {


    @GetExchange("/{project}/repos/{repo}/settings/hooks")
    ResponseEntity<HookPage> list(@PathVariable("project") String project,
                                  @PathVariable("repo") String repo,
                                  @Nullable @RequestParam(required = false, name = "start") Integer start,
                                  @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @GetExchange("/{project}/repos/{repo}/settings/hooks/{hookKey}")
    ResponseEntity<Hook> get(@PathVariable("project") String project,
                             @PathVariable("repo") String repo,
                             @PathVariable("hookKey") String hookKey);


    @PutExchange("/{project}/repos/{repo}/settings/hooks/{hookKey}/enabled")
    ResponseEntity<Hook> enable(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("hookKey") String hookKey);


    @DeleteExchange("/{project}/repos/{repo}/settings/hooks/{hookKey}/enabled")
    ResponseEntity<Hook> disable(@PathVariable("project") String project,
                                 @PathVariable("repo") String repo,
                                 @PathVariable("hookKey") String hookKey);
}
