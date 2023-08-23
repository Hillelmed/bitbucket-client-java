package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.annotations.*;
import io.github.hmedioni.bitbucket.client.domain.admin.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/api/latest/admin", accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
@Documentation({"https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-permission-management/#api-group-permission-management"})
public interface AdminApi {


    @GetExchange(value = "/groups/more-members")
    ResponseEntity<UserPage> listUsersByGroup(@RequestParam("context") String context,
                                              @Nullable @RequestParam(required = false, name = "filter") String filter,
                                              @Nullable @RequestParam(required = false, name = "start") Integer start,
                                              @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @GetExchange("/users")
    ResponseEntity<UserPage> listUsers(@Nullable @RequestParam(required = false, name = "filter") String filter,
                                       @Nullable @RequestParam(required = false, name = "start") Integer start,
                                       @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @GetExchange("/users")
    ResponseEntity<String> listUsersResponse(@Nullable @RequestParam(required = false, name = "filter") String filter,
                                             @Nullable @RequestParam(required = false, name = "start") Integer start,
                                             @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PostExchange("/users")
    ResponseEntity<Void> createUser(@RequestParam("name") String name,
                                    @RequestParam("password") String password,
                                    @RequestParam("displayName") String displayName,
                                    @RequestParam("emailAddress") String emailAddress,
                                    @Nullable @RequestParam(required = false, name = "addToDefaultGroup") Boolean addToDefaultGroup,
                                    @Nullable @RequestParam(required = false, name = "notify") String notify);


    @DeleteExchange("/users")
    ResponseEntity<User> deleteUser(@RequestParam("name") String name);
}
