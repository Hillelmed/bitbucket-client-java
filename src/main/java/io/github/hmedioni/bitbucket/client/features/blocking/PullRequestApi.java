package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.activities.*;
import io.github.hmedioni.bitbucket.client.domain.commit.*;
import io.github.hmedioni.bitbucket.client.domain.participants.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/api/latest/projects/", accept = "application/json", contentType = "application/json")
public interface PullRequestApi {


    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}")
    ResponseEntity<PullRequest> get(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @PathVariable("pullRequestId") int pullRequestId);


    @GetExchange("/{project}/repos/{repo}/pull-requests")
    ResponseEntity<PullRequestPage> list(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @Nullable @RequestParam(required = false, name = "direction") String direction,
                                         @Nullable @RequestParam(required = false, name = "at") String branchOrTag,
                                         @Nullable @RequestParam(required = false, name = "state") String state,
                                         @Nullable @RequestParam(required = false, name = "order") String order,
                                         @Nullable @RequestParam(required = false, name = "withAttributes") Boolean withAttributes,
                                         @Nullable @RequestParam(required = false, name = "withProperties") Boolean withProperties,
                                         @Nullable @RequestParam(required = false, name = "start") Integer start,
                                         @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    @PostExchange("/{project}/repos/{repo}/pull-requests")
    ResponseEntity<PullRequest> create(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @RequestBody CreatePullRequest createPullRequest);


    @PutExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}")
    ResponseEntity<PullRequest> edit(@PathVariable("project") String project,
                                     @PathVariable("repo") String repo,
                                     @PathVariable("pullRequestId") int pullRequestId,
                                     @RequestBody EditPullRequest editPullRequest);


    @DeleteExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("pullRequestId") long pullRequestId,
                                @RequestBody PullRequestVersion pullRequestVersion);


    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/merge")
    ResponseEntity<PullRequest> merge(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @PathVariable("pullRequestId") int pullRequestId,
                                      @RequestParam("version") int version);


    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/merge")
    ResponseEntity<MergeStatus> canMerge(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("pullRequestId") int pullRequestId);


    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/decline")
    ResponseEntity<PullRequest> decline(@PathVariable("project") String project,
                                        @PathVariable("repo") String repo,
                                        @PathVariable("pullRequestId") int pullRequestId,
                                        @RequestParam("version") int version);


    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/reopen")
    ResponseEntity<PullRequest> reopen(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("pullRequestId") int pullRequestId,
                                       @RequestParam("version") int version);


    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/changes")
    ResponseEntity<ChangePage> changes(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("pullRequestId") int pullRequestId,
                                       @Nullable @RequestParam(required = false, name = "withComments") Boolean withComments,
                                       @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                       @Nullable @RequestParam(required = false, name = "start") Integer start);


    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/commits")
    ResponseEntity<CommitPage> commits(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("pullRequestId") int pullRequestId,
                                       @Nullable @RequestParam(required = false, name = "withCounts") Boolean withCounts,
                                       @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                       @Nullable @RequestParam(required = false, name = "start") Integer start);


    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/activities")
    ResponseEntity<ActivitiesPage> listActivities(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @PathVariable("pullRequestId") long pullRequestId,
                                                  @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                                  @Nullable @RequestParam(required = false, name = "start") Integer start);


    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/participants")
    ResponseEntity<ParticipantsPage> listParticipants(@PathVariable("project") String project,
                                                      @PathVariable("repo") String repo,
                                                      @PathVariable("pullRequestId") long pullRequestId,
                                                      @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                                      @Nullable @RequestParam(required = false, name = "start") Integer start);


    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/participants")
    ResponseEntity<Participants> assignParticipant(@PathVariable("project") String project,
                                                   @PathVariable("repo") String repo,
                                                   @PathVariable("pullRequestId") long pullRequestId,
                                                   @RequestBody CreateParticipants participants);


    @DeleteExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/participants/{userSlug}")
    ResponseEntity<Void> deleteParticipant(@PathVariable("project") String project,
                                           @PathVariable("repo") String repo,
                                           @PathVariable("pullRequestId") long pullRequestId,
                                           @PathVariable("userSlug") String userSlug);


    @PutExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/participants/{userSlug}")
    ResponseEntity<Participants> addParticipant(@PathVariable("project") String project,
                                                @PathVariable("repo") String repo,
                                                @PathVariable("pullRequestId") long pullRequestId,
                                                @PathVariable("userSlug") String userSlug,
                                                @RequestBody CreateParticipants participants);
}
