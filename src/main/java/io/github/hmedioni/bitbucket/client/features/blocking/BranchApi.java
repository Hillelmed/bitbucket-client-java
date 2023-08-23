package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.branch.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.*;

@HttpExchange(url = "/rest", accept = "application/json", contentType = "application/json")
public interface BranchApi {


    @GetExchange("/api/latest/projects/{project}/repos/{repo}/branches")
    ResponseEntity<BranchPage> list(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @Nullable @RequestParam(required = false, name = "base") String base,
                                    @Nullable @RequestParam(required = false, name = "details") String details,
                                    @Nullable @RequestParam(required = false, name = "filterText") String filterText,
                                    @Nullable @RequestParam(required = false, name = "orderBy") String orderBy,
                                    @Nullable @RequestParam(required = false, name = "start") Integer start,
                                    @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PostExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branches")
    ResponseEntity<Branch> create(@PathVariable("project") String project,
                                  @PathVariable("repo") String repo,
                                  @RequestBody CreateBranch createBranch);


    @DeleteExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branches")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @RequestBody DeleteBranch branchPath);


    @PutExchange("/api/latest/projects/{project}/repos/{repo}/branches/default")
    ResponseEntity<Void> updateDefault(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @RequestBody UpdateDefaultBranch id);


    @GetExchange("/api/latest/projects/{project}/repos/{repo}/branches/default")
    ResponseEntity<Branch> getDefault(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo);


    @GetExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branchmodel")
    ResponseEntity<BranchModel> model(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo);


    @GetExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branches/info/{commitId}")
    ResponseEntity<BranchPage> info(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @PathVariable("commitId") String commitId);


    @GetExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branchmodel/configuration")
    ResponseEntity<BranchModelConfiguration> getModelConfiguration(@PathVariable("project") String project,
                                                                   @PathVariable("repo") String repo);


    @PutExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branchmodel/configuration")
    ResponseEntity<BranchModelConfiguration> updateModelConfiguration(@PathVariable("project") String project,
                                                                      @PathVariable("repo") String repo,
                                                                      @RequestBody CreateBranchModelConfiguration config);


    @DeleteExchange("/branch-utils/latest/projects/{project}/repos/{repo}/branchmodel/configuration")
    ResponseEntity<Void> deleteModelConfiguration(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo);


    @GetExchange("/branch-permissions/2.0/projects/{project}/repos/{repo}/restrictions")
    ResponseEntity<BranchRestrictionPage> listBranchRestriction(@PathVariable("project") String project,
                                                                @PathVariable("repo") String repo,
                                                                @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                                @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PostExchange(value = "/branch-permissions/2.0/projects/{project}/repos/{repo}/restrictions", accept = MediaType.APPLICATION_JSON_VALUE,
            contentType = "application/vnd.atl.bitbucket.bulk+json")
    ResponseEntity<List<BranchRestriction>> createBranchRestriction(@PathVariable("project") String project,
                                                                    @PathVariable("repo") String repo,
                                                                    @RequestBody List<BranchRestriction> branchRestrictions);


    @DeleteExchange("/branch-permissions/2.0/projects/{project}/repos/{repo}/restrictions/{id}")
    ResponseEntity<Void> deleteBranchRestriction(@PathVariable("project") String project,
                                                 @PathVariable("repo") String repo,
                                                 @PathVariable("id") long id);
}
