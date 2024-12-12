package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.annotations.Documentation;
import io.github.hillelmed.bitbucket.client.domain.sshkey.AccessKey;
import io.github.hillelmed.bitbucket.client.domain.sshkey.AccessKeyPage;
import io.github.hillelmed.bitbucket.client.domain.sshkey.Key;
import io.github.hillelmed.bitbucket.client.options.CreateAccessKey;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;


/**
 * The interface Keys api.
 */
@HttpExchange(url = "/rest", accept = "application/json", contentType = "application/json")
public interface KeysApi {


    /**
     * List by repo response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange("/keys/latest/projects/{project}/repos/{repo}/ssh")
    ResponseEntity<AccessKeyPage> listByRepo(@PathVariable("project") String project,
                                             @PathVariable("repo") String repo,
                                             @Nullable @RequestParam(required = false, name = "start") Integer start,
                                             @Nullable @RequestParam(required = false, name = "limit") Integer limit);

    /**
     * Create ssh key for user response entity.
     *
     * @param user the user
     * @param key  the key
     * @return the response entity
     */
    @Documentation("https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-authentication/#api-ssh-latest-keys-post")
    @PostExchange("/ssh/latest/keys")
    ResponseEntity<Key> createSSHKeyForUser(@RequestParam("user") String user, @RequestBody Key key);

    /**
     * Delete all ssh keys for user response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @Documentation("https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-authentication/#api-ssh-latest-keys-delete")
    @DeleteExchange("/ssh/latest/keys")
    ResponseEntity<Void> deleteAllSSHKeysForUser(@RequestParam("user") String user);

    /**
     * Create for repo response entity.
     *
     * @param project         the project
     * @param repo            the repo
     * @param createAccessKey the create access key
     * @return the response entity
     */
    @PostExchange("/keys/latest/projects/{project}/repos/{repo}/ssh")
    ResponseEntity<AccessKey> createForRepo(@PathVariable("project") String project,
                                            @PathVariable("repo") String repo,
                                            @RequestBody CreateAccessKey createAccessKey);


    /**
     * Gets for repo.
     *
     * @param project the project
     * @param repo    the repo
     * @param id      the id
     * @return the for repo
     */
    @GetExchange("/keys/latest/projects/{project}/repos/{repo}/ssh/{id}")
    ResponseEntity<AccessKey> getForRepo(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("id") long id);


    /**
     * Delete from repo response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param id      the id
     * @return the response entity
     */
    @DeleteExchange("/keys/latest/projects/{project}/repos/{repo}/ssh/{id}")
    ResponseEntity<Void> deleteFromRepo(@PathVariable("project") String project,
                                        @PathVariable("repo") String repo,
                                        @PathVariable("id") long id);


    /**
     * List by project response entity.
     *
     * @param project the project
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange("/keys/latest/projects/{project}/ssh")
    ResponseEntity<AccessKeyPage> listByProject(@PathVariable("project") String project,
                                                @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Create for project response entity.
     *
     * @param project         the project
     * @param createAccessKey the create access key
     * @return the response entity
     */
    @PostExchange("/keys/latest/projects/{project}/ssh")
    ResponseEntity<AccessKey> createForProject(@PathVariable("project") String project,
                                               @RequestBody CreateAccessKey createAccessKey);


    /**
     * Gets for project.
     *
     * @param project the project
     * @param id      the id
     * @return the for project
     */
    @GetExchange("/keys/latest/projects/{project}/ssh/{id}")
    ResponseEntity<AccessKey> getForProject(@PathVariable("project") String project,
                                            @PathVariable("id") long id);


    /**
     * Delete from project response entity.
     *
     * @param project the project
     * @param id      the id
     * @return the response entity
     */
    @DeleteExchange("/keys/latest/projects/{project}/ssh/{id}")
    ResponseEntity<Void> deleteFromProject(@PathVariable("project") String project,
                                           @PathVariable("id") long id);

}
