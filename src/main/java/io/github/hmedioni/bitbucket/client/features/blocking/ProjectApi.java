package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.project.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/api/latest/", accept = "application/json", contentType = "application/json")
public interface ProjectApi {


    @PostExchange("projects")
    ResponseEntity<Project> create(@RequestBody CreateProject createProject);


    @GetExchange("projects/{project}")
    ResponseEntity<Project> get(@PathVariable("project") String project);


    @DeleteExchange("projects/{project}")
    ResponseEntity<Void> delete(@PathVariable("project") String project);


    @GetExchange("projects")
    ResponseEntity<ProjectPage> list(@Nullable @RequestParam(required = false, name = "name") String name,
                                     @Nullable @RequestParam(required = false, name = "permission") String permission,
                                     @Nullable @RequestParam(required = false, name = "start") Integer start,
                                     @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PutExchange("projects/{project}/permissions/users")
    ResponseEntity<Void> createPermissionsByUser(@PathVariable("project") String project,
                                                 @RequestParam("permission") String permission,
                                                 @RequestParam("name") String name);


    @DeleteExchange("projects/{project}/permissions/users")
    ResponseEntity<Void> deletePermissionsByUser(@PathVariable("project") String project,
                                                 @RequestParam("name") String name);


    @GetExchange("projects/{project}/permissions/users")
    ProjectPermissionsPage listPermissionsByUser(@PathVariable("project") String project,
                                                 @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                 @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PutExchange("projects/{project}/permissions/groups")
    ResponseEntity<Void> createPermissionsByGroup(@PathVariable("project") String project,
                                                  @RequestParam("permission") String permission,
                                                  @RequestParam("name") String name);


    @DeleteExchange("projects/{project}/permissions/groups")
    ResponseEntity<Void> deletePermissionsByGroup(@PathVariable("project") String project,
                                                  @RequestParam("name") String name);


    @GetExchange("projects/{project}/permissions/groups")
    ResponseEntity<ProjectPermissionsPage> listPermissionsByGroup(@PathVariable("project") String project,
                                                                  @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                                  @Nullable @RequestParam(required = false, name = "limit") Integer limit);
}
