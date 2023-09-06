package io.github.hmedioni.bitbucket.client.features.blocking;


import io.github.hmedioni.bitbucket.client.annotations.*;
import io.github.hmedioni.bitbucket.client.domain.labels.*;
import io.github.hmedioni.bitbucket.client.domain.repository.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/api/latest/", accept = "application/json", contentType = "application/json")
public interface RepositoryApi {


    @PostExchange("projects/{project}/repos")
    ResponseEntity<Repository> create(@PathVariable("project") String project,
                                      @RequestBody CreateRepository createRepository);


    @GetExchange("projects/{project}/repos/{repo}")
    ResponseEntity<Repository> get(@PathVariable("project") String project,
                                   @PathVariable("repo") String repo);


    @PostExchange("projects/{project}/repos/{repo}")
    ResponseEntity<Repository> fork(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @RequestBody ForkRepository forkRepository);


    @DeleteExchange("projects/{project}/repos/{repo}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo);


    @GetExchange("projects/{project}/repos")
    ResponseEntity<RepositoryPage> list(@PathVariable("project") String project,
                                        @Nullable @RequestParam(required = false, name = "start") Integer start,
                                        @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @GetExchange("repos")
    ResponseEntity<RepositoryPage> listAll(@Nullable @RequestParam(required = false, name = "projectname") String project,
                                           @Nullable @RequestParam(required = false, name = "name") String repo,
                                           @Nullable @RequestParam(required = false, name = "permission") String permission,
                                           @Nullable @RequestParam(required = false, name = "visibility") String visibility,
                                           @Nullable @RequestParam(required = false, name = "start") Integer start,
                                           @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @GetExchange("projects/{project}/repos/{repo}/settings/pull-requests")
    PullRequestSettings getPullRequestSettings(@PathVariable("project") String project,
                                               @PathVariable("repo") String repo);


    @PostExchange("projects/{project}/repos/{repo}/settings/pull-requests")
    ResponseEntity<PullRequestSettings> updatePullRequestSettings(@PathVariable("project") String project,
                                                                  @PathVariable("repo") String repo,
                                                                  @RequestBody CreatePullRequestSettings createPullRequestSettings);


    @PutExchange("projects/{project}/repos/{repo}/permissions/users")
    ResponseEntity<Void> createPermissionsByUser(@PathVariable("project") String project,
                                                 @PathVariable("repo") String repo,
                                                 @RequestParam("permission") String permission,
                                                 @RequestParam("name") String name);


    @DeleteExchange("projects/{project}/repos/{repo}/permissions/users")
    ResponseEntity<Void> deletePermissionsByUser(@PathVariable("project") String project,
                                                 @PathVariable("repo") String repo,
                                                 @RequestParam("name") String name);


    @GetExchange("projects/{project}/repos/{repo}/permissions/users")
    ResponseEntity<PermissionsPage> listPermissionsByUser(@PathVariable("project") String project,
                                                          @PathVariable("repo") String repo,
                                                          @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                          @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PutExchange("projects/{project}/repos/{repo}/permissions/groups")
    ResponseEntity<Void> createPermissionsByGroup(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @RequestParam("permission") String permission,
                                                  @RequestParam("name") String name);


    @DeleteExchange("projects/{project}/repos/{repo}/permissions/groups")
    ResponseEntity<Void> deletePermissionsByGroup(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @RequestParam("name") String name);


    @GetExchange("projects/{project}/repos/{repo}/permissions/groups")
    ResponseEntity<PermissionsPage> listPermissionsByGroup(@PathVariable("project") String project,
                                                           @PathVariable("repo") String repo,
                                                           @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                           @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @GetExchange("projects/{project}/repos/{repo}/labels")
    ResponseEntity<LabelsPage> getLabels(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo);

    @Documentation("https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-repository/#api-api-latest-projects-projectkey-repos-repositoryslug-attachments-attachmentid-metadata-put")
    @PutExchange("projects/{project}/repos/{repo}/attachments/{attachmentId}/metadata")
    ResponseEntity<String> saveAttachmentMetadata(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @PathVariable("attachmentId") String attachmentId,
                                                  @RequestBody String content);
}
