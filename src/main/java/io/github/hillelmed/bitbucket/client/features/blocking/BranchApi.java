package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.domain.branch.*;
import io.github.hillelmed.bitbucket.client.options.CreateBranch;
import io.github.hillelmed.bitbucket.client.options.CreateBranchModelConfiguration;
import io.github.hillelmed.bitbucket.client.options.DeleteBranch;
import io.github.hillelmed.bitbucket.client.options.UpdateDefaultBranch;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;

import java.util.List;

/**
 * The interface Branch api.
 */
@HttpExchange(url = "/rest", accept = "application/json", contentType = "application/json")
public interface BranchApi {


    /**
     * List response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param base       the base
     * @param details    the details
     * @param filterText the filter text
     * @param orderBy    the order by
     * @param start      the start
     * @param limit      the limit
     * @return the response entity
     */
    @GetExchange("/api/latest/projects/{project}/repos/{repo}/branches")
    ResponseEntity<BranchPage> list(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @Nullable @RequestParam(required = false, name = "base") String base,
                                    @Nullable @RequestParam(required = false, name = "details") String details,
                                    @Nullable @RequestParam(required = false, name = "filterText") String filterText,
                                    @Nullable @RequestParam(required = false, name = "orderBy") String orderBy,
                                    @Nullable @RequestParam(required = false, name = "start") Integer start,
                                    @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Create response entity.
     *
     * @param project      the project
     * @param repo         the repo
     * @param createBranch the create branch
     * @return the response entity
     */
    @PostExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branches")
    ResponseEntity<Branch> create(@PathVariable("project") String project,
                                  @PathVariable("repo") String repo,
                                  @RequestBody CreateBranch createBranch);


    /**
     * Delete response entity.
     *
     * @param project    the project
     * @param repo       the repo
     * @param branchPath the branch path
     * @return the response entity
     */
    @DeleteExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branches")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @RequestBody DeleteBranch branchPath);


    /**
     * Update default response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param id      the id
     * @return the response entity
     */
    @PutExchange("/api/latest/projects/{project}/repos/{repo}/branches/default")
    ResponseEntity<Void> updateDefault(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @RequestBody UpdateDefaultBranch id);


    /**
     * Gets default.
     *
     * @param project the project
     * @param repo    the repo
     * @return the default
     */
    @GetExchange("/api/latest/projects/{project}/repos/{repo}/branches/default")
    ResponseEntity<Branch> getDefault(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo);


    /**
     * Model response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @return the response entity
     */
    @GetExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branchmodel")
    ResponseEntity<BranchModel> model(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo);


    /**
     * Info response entity.
     *
     * @param project  the project
     * @param repo     the repo
     * @param commitId the commit id
     * @return the response entity
     */
    @GetExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branches/info/{commitId}")
    ResponseEntity<BranchPage> info(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @PathVariable("commitId") String commitId);


    /**
     * Gets model configuration.
     *
     * @param project the project
     * @param repo    the repo
     * @return the model configuration
     */
    @GetExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branchmodel/configuration")
    ResponseEntity<BranchModelConfiguration> getModelConfiguration(@PathVariable("project") String project,
                                                                   @PathVariable("repo") String repo);


    /**
     * Update model configuration response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param config  the config
     * @return the response entity
     */
    @PutExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branchmodel/configuration")
    ResponseEntity<BranchModelConfiguration> updateModelConfiguration(@PathVariable("project") String project,
                                                                      @PathVariable("repo") String repo,
                                                                      @RequestBody CreateBranchModelConfiguration config);


    /**
     * Delete model configuration response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @return the response entity
     */
    @DeleteExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branchmodel/configuration")
    ResponseEntity<Void> deleteModelConfiguration(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo);


    /**
     * List branch restriction response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param start   the start
     * @param limit   the limit
     * @return the response entity
     */
    @GetExchange("/branch-permissions/2.0/projects/{project}/repos/{repo}/restrictions")
    ResponseEntity<BranchRestrictionPage> listBranchRestriction(@PathVariable("project") String project,
                                                                @PathVariable("repo") String repo,
                                                                @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                                @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Create branch restriction response entity.
     *
     * @param project            the project
     * @param repo               the repo
     * @param branchRestrictions the branch restrictions
     * @return the response entity
     */
    @PostExchange(value = "/branch-permissions/2.0/projects/{project}/repos/{repo}/restrictions", accept = MediaType.APPLICATION_JSON_VALUE,
            contentType = "application/vnd.atl.bitbucket.bulk+json")
    ResponseEntity<List<BranchRestriction>> createBranchRestriction(@PathVariable("project") String project,
                                                                    @PathVariable("repo") String repo,
                                                                    @RequestBody List<BranchRestriction> branchRestrictions);


    /**
     * Delete branch restriction response entity.
     *
     * @param project the project
     * @param repo    the repo
     * @param id      the id
     * @return the response entity
     */
    @DeleteExchange("/branch-permissions/2.0/projects/{project}/repos/{repo}/restrictions/{id}")
    ResponseEntity<Void> deleteBranchRestriction(@PathVariable("project") String project,
                                                 @PathVariable("repo") String repo,
                                                 @PathVariable("id") long id);
}
