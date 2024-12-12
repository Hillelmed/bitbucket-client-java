package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.domain.insights.Annotation;
import io.github.hillelmed.bitbucket.client.domain.insights.AnnotationsResponse;
import io.github.hillelmed.bitbucket.client.domain.insights.InsightReport;
import io.github.hillelmed.bitbucket.client.domain.insights.InsightReportPage;
import io.github.hillelmed.bitbucket.client.options.CreateAnnotations;
import io.github.hillelmed.bitbucket.client.options.CreateInsightReport;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;


/**
 * The interface Insights api.
 */
@HttpExchange(url = "/rest/insights/latest", accept = "application/json", contentType = "application/json")
public interface InsightsApi {


    /**
     * List annotations response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param commitId   the commit id
     * @param externalId the external id
     * @param path       the path
     * @param severity   the severity
     * @param type       the type
     * @return the response entity
     */
    @GetExchange("/projects/{project}/repos/{repo}/commits/{commitId}/annotations")
    ResponseEntity<AnnotationsResponse> listAnnotations(@PathVariable("project") String project,
                                                        @PathVariable("repo") String repo,
                                                        @PathVariable("commitId") String commitId,
                                                        @Nullable @RequestParam(required = false, name = "externalId") String externalId,
                                                        @Nullable @RequestParam(required = false, name = "path") String path,
                                                        @Nullable @RequestParam(required = false, name = "severity") String severity,
                                                        @Nullable @RequestParam(required = false, name = "type") String type);


    /**
     * List reports response entity.
     *
     * @param project  the project
     * @param repo     the repo
     * @param commitId the commit id
     * @param limit    the limit
     * @param start    the start
     * @return the response entity
     */
    @GetExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports")
    ResponseEntity<InsightReportPage> listReports(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @PathVariable("commitId") String commitId,
                                                  @Nullable @RequestParam(required = false, name = "limit") int limit,
                                                  @Nullable @RequestParam(required = false, name = "start") int start);


    /**
     * Gets report.
     *
     * @param project  the project
     * @param repo     the repo
     * @param commitId the commit id
     * @param key      the key
     * @return the report
     */
    @GetExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}")
    ResponseEntity<InsightReport> getReport(@PathVariable("project") String project,
                                            @PathVariable("repo") String repo,
                                            @PathVariable("commitId") String commitId,
                                            @PathVariable("key") String key);


    /**
     * Create report response entity.
     *
     * @param project             the project
     * @param repo                the repo
     * @param commitId            the commit id
     * @param key                 the key
     * @param createInsightReport the create insight report
     * @return the response entity
     */
    @PutExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}")
    ResponseEntity<InsightReport> createReport(@PathVariable("project") String project,
                                               @PathVariable("repo") String repo,
                                               @PathVariable("commitId") String commitId,
                                               @PathVariable("key") String key,
                                               @RequestBody CreateInsightReport createInsightReport);


    /**
     * Delete report response entity.
     *
     * @param project  the project
     * @param repo     the repo
     * @param commitId the commit id
     * @param key      the key
     * @return the response entity
     */
    @DeleteExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}")
    ResponseEntity<Void> deleteReport(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @PathVariable("commitId") String commitId,
                                      @PathVariable("key") String key);


    /**
     * Delete annotation response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param commitId   the commit id
     * @param key        the key
     * @param externalId the external id
     * @return the response entity
     */
    @DeleteExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}/annotations")
    ResponseEntity<Void> deleteAnnotation(@PathVariable("project") String project,
                                          @PathVariable("repo") String repo,
                                          @PathVariable("commitId") String commitId,
                                          @PathVariable("key") String key,
                                          @Nullable @RequestParam(required = false, name = "externalId") String externalId);


    /**
     * Create annotations response entity.
     *
     * @param project           the project
     * @param repo              the repo
     * @param commitId          the commit id
     * @param key               the key
     * @param createAnnotations the create annotations
     * @return the response entity
     */
    @PostExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}/annotations")
    ResponseEntity<Void> createAnnotations(@PathVariable("project") String project,
                                           @PathVariable("repo") String repo,
                                           @PathVariable("commitId") String commitId,
                                           @PathVariable("key") String key,
                                           @RequestBody CreateAnnotations createAnnotations);


    /**
     * Gets annotations by report.
     *
     * @param project  the project
     * @param repo     the repo
     * @param commitId the commit id
     * @param key      the key
     * @return the annotations by report
     */
    @GetExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}/annotations")
    ResponseEntity<AnnotationsResponse> getAnnotationsByReport(@PathVariable("project") String project,
                                                               @PathVariable("repo") String repo,
                                                               @PathVariable("commitId") String commitId,
                                                               @PathVariable("key") String key);


    /**
     * Create annotation response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param commitId   the commit id
     * @param key        the key
     * @param externalId the external id
     * @param annotation the annotation
     * @return the response entity
     */
    @PutExchange("/projects/{project}/repos/{repo}/commits/{commitId}/reports/{key}/annotations/{externalId}")
    ResponseEntity<Void> createAnnotation(@PathVariable("project") String project,
                                          @PathVariable("repo") String repo,
                                          @PathVariable("commitId") String commitId,
                                          @PathVariable("key") String key,
                                          @PathVariable("externalId") String externalId,
                                          @RequestBody Annotation annotation);
}
