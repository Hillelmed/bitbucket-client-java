package io.github.hillelmed.bitbucket.client.features.blocking;


import io.github.hillelmed.bitbucket.client.domain.defaultreviewers.Condition;
import io.github.hillelmed.bitbucket.client.options.CreateCondition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

import java.util.List;

/**
 * The interface Default reviewers api.
 */
@HttpExchange(url = "/rest/default-reviewers/latest/projects", accept = "application/json", contentType = "application/json")
public interface DefaultReviewersApi {


    /**
     * List conditions response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/conditions")
    ResponseEntity<List<Condition>> listConditions(@PathVariable("project") String project,
                                                   @PathVariable("repo") String repo);


    /**
     * Create condition response entity.
     *
     * @param project   the project
     * @param repo      the repo
     * @param condition the condition
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/condition")
    ResponseEntity<Condition> createCondition(@PathVariable("project") String project,
                                              @PathVariable("repo") String repo,
                                              @RequestBody CreateCondition condition);


    /**
     * Update condition response entity.
     *
     * @param project   the project
     * @param repo      the repo
     * @param id        the id
     * @param condition the condition
     * @return the response entity
     */
    @PutExchange("/{project}/repos/{repo}/condition/{id}")
    ResponseEntity<Condition> updateCondition(@PathVariable("project") String project,
                                              @PathVariable("repo") String repo,
                                              @PathVariable("id") long id,
                                              @RequestBody CreateCondition condition);


    /**
     * Delete condition response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param id      the id
     * @return the response entity
     */
    @DeleteExchange("/{project}/repos/{repo}/condition/{id}")
    ResponseEntity<Void> deleteCondition(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("id") long id);

}
