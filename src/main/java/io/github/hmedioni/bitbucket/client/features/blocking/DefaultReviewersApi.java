package io.github.hmedioni.bitbucket.client.features.blocking;


import io.github.hmedioni.bitbucket.client.domain.defaultreviewers.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.*;

@HttpExchange(url = "/rest/default-reviewers/latest/projects", accept = "application/json", contentType = "application/json")
public interface DefaultReviewersApi {


    @GetExchange("/{project}/repos/{repo}/conditions")
    ResponseEntity<List<Condition>> listConditions(@PathVariable("project") String project,
                                                   @PathVariable("repo") String repo);


    @PostExchange("/{project}/repos/{repo}/condition")
    ResponseEntity<Condition> createCondition(@PathVariable("project") String project,
                                              @PathVariable("repo") String repo,
                                              @RequestBody CreateCondition condition);


    @PutExchange("/{project}/repos/{repo}/condition/{id}")
    ResponseEntity<Condition> updateCondition(@PathVariable("project") String project,
                                              @PathVariable("repo") String repo,
                                              @PathVariable("id") long id,
                                              @RequestBody CreateCondition condition);


    @DeleteExchange("/{project}/repos/{repo}/condition/{id}")
    ResponseEntity<Void> deleteCondition(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("id") long id);

}
