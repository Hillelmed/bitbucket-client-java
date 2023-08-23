package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.insights.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/insights/latest", accept = "application/json", contentType = "application/json")
public interface InsightsApi {


    @GetExchange("/projects/{project}/repos/{repo}/commits/{commitId}/annotations")
    ResponseEntity<AnnotationsResponse> listAnnotations(@PathVariable("project") String project,
                                                        @PathVariable("repo") String repo,
                                                        @PathVariable("commitId") String commitId,
                                                        @Nullable @RequestParam(required = false, name = "externalId") String externalId,
                                                        @Nullable @RequestParam(required = false, name = "path") String path,
                                                        @Nullable @RequestParam(required = false, name = "severity") String severity,
                                                        @Nullable @RequestParam(required = false, name = "type") String type);


    @GetExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports")
    ResponseEntity<InsightReportPage> listReports(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @PathVariable("commitId") String commitId,
                                                  @Nullable @RequestParam(required = false, name = "limit") int limit,
                                                  @Nullable @RequestParam(required = false, name = "start") int start);


    @GetExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}")
    ResponseEntity<InsightReport> getReport(@PathVariable("project") String project,
                                            @PathVariable("repo") String repo,
                                            @PathVariable("commitId") String commitId,
                                            @PathVariable("key") String key);


    @PutExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}")
    ResponseEntity<InsightReport> createReport(@PathVariable("project") String project,
                                               @PathVariable("repo") String repo,
                                               @PathVariable("commitId") String commitId,
                                               @PathVariable("key") String key,
                                               @RequestBody CreateInsightReport createInsightReport);


    @DeleteExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}")
    ResponseEntity<Void> deleteReport(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @PathVariable("commitId") String commitId,
                                      @PathVariable("key") String key);


    @DeleteExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}/annotations")
    ResponseEntity<Void> deleteAnnotation(@PathVariable("project") String project,
                                          @PathVariable("repo") String repo,
                                          @PathVariable("commitId") String commitId,
                                          @PathVariable("key") String key,
                                          @Nullable @RequestParam(required = false, name = "externalId") String externalId);


    @PostExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}/annotations")
    ResponseEntity<Void> createAnnotations(@PathVariable("project") String project,
                                           @PathVariable("repo") String repo,
                                           @PathVariable("commitId") String commitId,
                                           @PathVariable("key") String key,
                                           @RequestBody CreateAnnotations createAnnotations);


    @GetExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}/annotations")
    ResponseEntity<AnnotationsResponse> getAnnotationsByReport(@PathVariable("project") String project,
                                                               @PathVariable("repo") String repo,
                                                               @PathVariable("commitId") String commitId,
                                                               @PathVariable("key") String key);


    @PutExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}/annotations/{externalId}")
    ResponseEntity<Void> createAnnotation(@PathVariable("project") String project,
                                          @PathVariable("repo") String repo,
                                          @PathVariable("commitId") String commitId,
                                          @PathVariable("key") String key,
                                          @PathVariable("externalId") String externalId,
                                          @RequestBody Annotation annotation);
}
