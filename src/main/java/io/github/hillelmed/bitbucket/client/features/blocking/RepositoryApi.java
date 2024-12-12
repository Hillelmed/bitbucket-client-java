package io.github.hillelmed.bitbucket.client.features.blocking;


import io.github.hillelmed.bitbucket.client.annotations.Documentation;
import io.github.hillelmed.bitbucket.client.domain.labels.LabelsPage;
import io.github.hillelmed.bitbucket.client.domain.repository.*;
import io.github.hillelmed.bitbucket.client.options.CreatePullRequestSettings;
import io.github.hillelmed.bitbucket.client.options.CreateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;


/**
 * The interface Repository api.
 */
@HttpExchange(url = "/rest/api/latest/", accept = "application/json", contentType = "application/json")
public interface RepositoryApi {


    /**
     * Create response entity.
     *
     * @param project          the project
     * @param createRepository the create repository
     * @return the response entity
     */
    @PostExchange("projects/{project}/repos")
    ResponseEntity<Repository> create(@PathVariable("project") String project,
                                      @RequestBody CreateRepository createRepository);


    /**
     * Get response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @return the response entity
     */
    @GetExchange("projects/{project}/repos/{repo}")
    ResponseEntity<Repository> get(@PathVariable("project") String project,
                                   @PathVariable("repo") String repo);


    /**
     * Fork response entity.
     *
     * @param project        the project
     * @param repo           the repo
     * @param forkRepository the fork repository
     * @return the response entity
     */
    @PostExchange("projects/{project}/repos/{repo}")
    ResponseEntity<Repository> fork(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @RequestBody ForkRepository forkRepository);


    /**
     * Delete response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @return the response entity
     */
    @DeleteExchange("projects/{project}/repos/{repo}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo);


    /**
     * List response entity.
     *
     * @param project the project
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange("projects/{project}/repos")
    ResponseEntity<RepositoryPage> list(@PathVariable("project") String project,
                                        @Nullable @RequestParam(required = false, name = "start") Integer start,
                                        @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * List all response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param permission the permission
     * @param visibility the visibility
     * @param start      the start
     * @param limit      the limit
     * @return the response entity
     */
    @GetExchange("repos")
    ResponseEntity<RepositoryPage> listAll(@Nullable @RequestParam(required = false, name = "projectname") String project,
                                           @Nullable @RequestParam(required = false, name = "name") String repo,
                                           @Nullable @RequestParam(required = false, name = "permission") String permission,
                                           @Nullable @RequestParam(required = false, name = "visibility") String visibility,
                                           @Nullable @RequestParam(required = false, name = "start") Integer start,
                                           @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Gets pull request settings.
     *
     * @param project the project
     * @param repo    the repo
     * @return the pull request settings
     */
    @GetExchange("projects/{project}/repos/{repo}/settings/pull-requests")
    PullRequestSettings getPullRequestSettings(@PathVariable("project") String project,
                                               @PathVariable("repo") String repo);


    /**
     * Update pull request settings response entity.
     *
     * @param project                   the project
     * @param repo                      the repo
     * @param createPullRequestSettings the create pull request settings
     * @return the response entity
     */
    @PostExchange("projects/{project}/repos/{repo}/settings/pull-requests")
    ResponseEntity<PullRequestSettings> updatePullRequestSettings(@PathVariable("project") String project,
                                                                  @PathVariable("repo") String repo,
                                                                  @RequestBody CreatePullRequestSettings createPullRequestSettings);


    /**
     * Create permissions by user response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param permission the permission
     * @param name       the name
     * @return the response entity
     */
    @PutExchange("projects/{project}/repos/{repo}/permissions/users")
    ResponseEntity<Void> createPermissionsByUser(@PathVariable("project") String project,
                                                 @PathVariable("repo") String repo,
                                                 @RequestParam("permission") String permission,
                                                 @RequestParam("name") String name);


    /**
     * Delete permissions by user response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param name    the name
     * @return the response entity
     */
    @DeleteExchange("projects/{project}/repos/{repo}/permissions/users")
    ResponseEntity<Void> deletePermissionsByUser(@PathVariable("project") String project,
                                                 @PathVariable("repo") String repo,
                                                 @RequestParam("name") String name);


    /**
     * List permissions by user response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange("projects/{project}/repos/{repo}/permissions/users")
    ResponseEntity<PermissionsPage> listPermissionsByUser(@PathVariable("project") String project,
                                                          @PathVariable("repo") String repo,
                                                          @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                          @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Create permissions by group response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param permission the permission
     * @param name       the name
     * @return the response entity
     */
    @PutExchange("projects/{project}/repos/{repo}/permissions/groups")
    ResponseEntity<Void> createPermissionsByGroup(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @RequestParam("permission") String permission,
                                                  @RequestParam("name") String name);


    /**
     * Delete permissions by group response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param name    the name
     * @return the response entity
     */
    @DeleteExchange("projects/{project}/repos/{repo}/permissions/groups")
    ResponseEntity<Void> deletePermissionsByGroup(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @RequestParam("name") String name);


    /**
     * List permissions by group response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange("projects/{project}/repos/{repo}/permissions/groups")
    ResponseEntity<PermissionsPage> listPermissionsByGroup(@PathVariable("project") String project,
                                                           @PathVariable("repo") String repo,
                                                           @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                           @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Gets labels.
     *
     * @param project the project
     * @param repo    the repo
     * @return the labels
     */
    @GetExchange("projects/{project}/repos/{repo}/labels")
    ResponseEntity<LabelsPage> getLabels(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo);

    /**
     * Save attachment metadata response entity.
     *
     * @param project      the project
     * @param repo         the repo
     * @param attachmentId the attachment id
     * @param content      the content
     * @return the response entity
     */
    @Documentation("https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-repository/#api-api-latest-projects-projectkey-repos-repositoryslug-attachments-attachmentid-metadata-put")
    @PutExchange("projects/{project}/repos/{repo}/attachments/{attachmentId}/metadata")
    ResponseEntity<String> saveAttachmentMetadata(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @PathVariable("attachmentId") String attachmentId,
                                                  @RequestBody String content);
}
