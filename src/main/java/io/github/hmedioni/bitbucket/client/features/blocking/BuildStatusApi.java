package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.build.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/build-status/latest", accept = "application/json", contentType = "application/json")
public interface BuildStatusApi {


    @GetExchange("/commits/{commitId}")
    ResponseEntity<StatusPage> status(@PathVariable("commitId") String commitId,
                                      @Nullable @RequestParam(required = false, name = "start") Integer start,
                                      @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PostExchange("/commits/{commitId}")
    ResponseEntity<Void> add(@PathVariable("commitId") String commitId,
                             @RequestBody CreateBuildStatus createBuildStatus);


    @GetExchange("/commits/stats/{commitId}")
    ResponseEntity<Summary> summary(@PathVariable("commitId") String commitId);
}
