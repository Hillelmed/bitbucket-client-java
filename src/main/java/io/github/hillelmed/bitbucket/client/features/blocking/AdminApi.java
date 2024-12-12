package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.annotations.Documentation;
import io.github.hillelmed.bitbucket.client.domain.admin.UserPage;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;


/**
 * The interface Admin api.
 */
@HttpExchange(url = "/rest/api/latest/admin", accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
@Documentation({"https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-permission-management/#api-group-permission-management"})
public interface AdminApi {


    /**
     * List users by group response entity.
     *
     * @param context the context
     * @param filter  the filter
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange(value = "/groups/more-members")
    ResponseEntity<UserPage> listUsersByGroup(@RequestParam("context") String context,
                                              @Nullable @RequestParam(required = false, name = "filter") String filter,
                                              @Nullable @RequestParam(required = false, name = "start") Integer start,
                                              @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * List users response entity.
     *
     * @param filter the filter
     * @param start  the start
     * @param limit  the limit
     * @return the response entity
     */
    @GetExchange("/users")
    ResponseEntity<UserPage> listUsers(@Nullable @RequestParam(required = false, name = "filter") String filter,
                                       @Nullable @RequestParam(required = false, name = "start") Integer start,
                                       @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * List users response response entity.
     *
     * @param filter the filter
     * @param start  the start
     * @param limit  the limit
     * @return the response entity
     */
    @GetExchange("/users")
    ResponseEntity<String> listUsersResponse(@Nullable @RequestParam(required = false, name = "filter") String filter,
                                             @Nullable @RequestParam(required = false, name = "start") Integer start,
                                             @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Create user response entity.
     *
     * @param name              the name
     * @param password          the password
     * @param displayName       the display name
     * @param emailAddress      the email address
     * @param addToDefaultGroup the add to default group
     * @param notify            the notify
     * @return the response entity
     */
    @PostExchange("/users")
    ResponseEntity<Void> createUser(@RequestParam("name") String name,
                                    @RequestParam("password") String password,
                                    @RequestParam("displayName") String displayName,
                                    @RequestParam("emailAddress") String emailAddress,
                                    @Nullable @RequestParam(required = false, name = "addToDefaultGroup") Boolean addToDefaultGroup,
                                    @Nullable @RequestParam(required = false, name = "notify") String notify);


    /**
     * Delete user response entity.
     *
     * @param name the name
     * @return the response entity
     */
    @DeleteExchange("/users")
    ResponseEntity<User> deleteUser(@RequestParam("name") String name);
}
