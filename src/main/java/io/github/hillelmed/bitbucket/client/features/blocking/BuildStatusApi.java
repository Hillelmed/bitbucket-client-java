package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.domain.build.StatusPage;
import io.github.hillelmed.bitbucket.client.domain.build.Summary;
import io.github.hillelmed.bitbucket.client.options.CreateBuildStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;


/**
 * The interface Build status api.
 */
@HttpExchange(url = "/rest/build-status/latest", accept = "application/json", contentType = "application/json")
public interface BuildStatusApi {


    /**
     * Status response entity.
     *
     * @param commitId the commit id
     * @param start    the start
     * @param limit    the limit
     * @return the response entity
     */
    @GetExchange("/commits/{commitId}")
    ResponseEntity<StatusPage> status(@PathVariable("commitId") String commitId,
                                      @Nullable @RequestParam(required = false, name = "start") Integer start,
                                      @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Add response entity.
     *
     * @param commitId          the commit id
     * @param createBuildStatus the create build status
     * @return the response entity
     */
    @PostExchange("/commits/{commitId}")
    ResponseEntity<Void> add(@PathVariable("commitId") String commitId,
                             @RequestBody CreateBuildStatus createBuildStatus);


    /**
     * Summary response entity.
     *
     * @param commitId the commit id
     * @return the response entity
     */
    @GetExchange("/commits/stats/{commitId}")
    ResponseEntity<Summary> summary(@PathVariable("commitId") String commitId);
}
