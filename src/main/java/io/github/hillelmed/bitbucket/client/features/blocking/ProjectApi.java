package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.domain.project.Project;
import io.github.hillelmed.bitbucket.client.domain.project.ProjectPage;
import io.github.hillelmed.bitbucket.client.domain.project.ProjectPermissionsPage;
import io.github.hillelmed.bitbucket.client.options.CreateProject;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;


/**
 * The interface Project api.
 */
@HttpExchange(url = "/rest/api/latest/", accept = "application/json", contentType = "application/json")
public interface ProjectApi {


    /**
     * Create response entity.
     *
     * @param createProject the create project
     * @return the response entity
     */
    @PostExchange("projects")
    ResponseEntity<Project> create(@RequestBody CreateProject createProject);


    /**
     * Get response entity.
     *
     * @param project the project
     * @return the response entity
     */
    @GetExchange("projects/{project}")
    ResponseEntity<Project> get(@PathVariable("project") String project);


    /**
     * Delete response entity.
     *
     * @param project the project
     * @return the response entity
     */
    @DeleteExchange("projects/{project}")
    ResponseEntity<Void> delete(@PathVariable("project") String project);


    /**
     * List response entity.
     *
     * @param name       the name
     * @param permission the permission
     * @param start      the start
     * @param limit      the limit
     * @return the response entity
     */
    @GetExchange("projects")
    ResponseEntity<ProjectPage> list(@Nullable @RequestParam(required = false, name = "name") String name,
                                     @Nullable @RequestParam(required = false, name = "permission") String permission,
                                     @Nullable @RequestParam(required = false, name = "start") Integer start,
                                     @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Create permissions by user response entity.
     *
     * @param project    the project
     * @param permission the permission
     * @param name       the name
     * @return the response entity
     */
    @PutExchange("projects/{project}/permissions/users")
    ResponseEntity<Void> createPermissionsByUser(@PathVariable("project") String project,
                                                 @RequestParam("permission") String permission,
                                                 @RequestParam("name") String name);


    /**
     * Delete permissions by user response entity.
     *
     * @param project the project
     * @param name    the name
     * @return the response entity
     */
    @DeleteExchange("projects/{project}/permissions/users")
    ResponseEntity<Void> deletePermissionsByUser(@PathVariable("project") String project,
                                                 @RequestParam("name") String name);


    /**
     * List permissions by user project permissions page.
     *
     * @param project the project
     * @param start   the start
     * @param limit   the limit
     * @return the project permissions page
     */
    @GetExchange("projects/{project}/permissions/users")
    ProjectPermissionsPage listPermissionsByUser(@PathVariable("project") String project,
                                                 @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                 @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Create permissions by group response entity.
     *
     * @param project    the project
     * @param permission the permission
     * @param name       the name
     * @return the response entity
     */
    @PutExchange("projects/{project}/permissions/groups")
    ResponseEntity<Void> createPermissionsByGroup(@PathVariable("project") String project,
                                                  @RequestParam("permission") String permission,
                                                  @RequestParam("name") String name);


    /**
     * Delete permissions by group response entity.
     *
     * @param project the project
     * @param name    the name
     * @return the response entity
     */
    @DeleteExchange("projects/{project}/permissions/groups")
    ResponseEntity<Void> deletePermissionsByGroup(@PathVariable("project") String project,
                                                  @RequestParam("name") String name);


    /**
     * List permissions by group response entity.
     *
     * @param project the project
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange("projects/{project}/permissions/groups")
    ResponseEntity<ProjectPermissionsPage> listPermissionsByGroup(@PathVariable("project") String project,
                                                                  @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                                  @Nullable @RequestParam(required = false, name = "limit") Integer limit);
}
