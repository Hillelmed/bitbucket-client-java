package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.annotations.*;
import io.github.hmedioni.bitbucket.client.domain.sshkey.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest", accept = "application/json", contentType = "application/json")
public interface KeysApi {


    @GetExchange("/keys/latest/projects/{project}/repos/{repo}/ssh")
    ResponseEntity<AccessKeyPage> listByRepo(@PathVariable("project") String project,
                                             @PathVariable("repo") String repo,
                                             @Nullable @RequestParam(required = false, name = "start") Integer start,
                                             @Nullable @RequestParam(required = false, name = "limit") Integer limit);

    @Documentation("https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-authentication/#api-ssh-latest-keys-post")
    @PostExchange("/ssh/latest/keys")
    ResponseEntity<Key> createSSHKeyForUser(@RequestParam("user") String user,@RequestBody Key key);

    @Documentation("https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-authentication/#api-ssh-latest-keys-delete")
    @DeleteExchange("/ssh/latest/keys")
    ResponseEntity<Void> deleteAllSSHKeysForUser(@RequestParam("user") String user);

    @PostExchange("/keys/latest/projects/{project}/repos/{repo}/ssh")
    ResponseEntity<AccessKey> createForRepo(@PathVariable("project") String project,
                                            @PathVariable("repo") String repo,
                                            @RequestBody CreateAccessKey createAccessKey);


    @GetExchange("/keys/latest/projects/{project}/repos/{repo}/ssh/{id}")
    ResponseEntity<AccessKey> getForRepo(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("id") long id);


    @DeleteExchange("/keys/latest/projects/{project}/repos/{repo}/ssh/{id}")
    ResponseEntity<Void> deleteFromRepo(@PathVariable("project") String project,
                                        @PathVariable("repo") String repo,
                                        @PathVariable("id") long id);


    @GetExchange("/keys/latest/projects/{project}/ssh")
    ResponseEntity<AccessKeyPage> listByProject(@PathVariable("project") String project,
                                                @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PostExchange("/keys/latest/projects/{project}/ssh")
    ResponseEntity<AccessKey> createForProject(@PathVariable("project") String project,
                                               @RequestBody CreateAccessKey createAccessKey);


    @GetExchange("/keys/latest/projects/{project}/ssh/{id}")
    ResponseEntity<AccessKey> getForProject(@PathVariable("project") String project,
                                            @PathVariable("id") long id);


    @DeleteExchange("/keys/latest/projects/{project}/ssh/{id}")
    ResponseEntity<Void> deleteFromProject(@PathVariable("project") String project,
                                           @PathVariable("id") long id);

}
