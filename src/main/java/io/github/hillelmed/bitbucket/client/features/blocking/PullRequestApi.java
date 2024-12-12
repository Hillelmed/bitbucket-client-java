package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.annotations.Documentation;
import io.github.hillelmed.bitbucket.client.domain.activities.ActivitiesPage;
import io.github.hillelmed.bitbucket.client.domain.commit.CommitPage;
import io.github.hillelmed.bitbucket.client.domain.participants.Participants;
import io.github.hillelmed.bitbucket.client.domain.participants.ParticipantsPage;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.*;
import io.github.hillelmed.bitbucket.client.options.CreateParticipants;
import io.github.hillelmed.bitbucket.client.options.CreatePullRequest;
import io.github.hillelmed.bitbucket.client.options.EditPullRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;


/**
 * The interface Pull request api.
 */
@HttpExchange(url = "/rest/api/latest/projects/", accept = "application/json", contentType = "application/json")
public interface PullRequestApi {


    /**
     * Get response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}")
    ResponseEntity<PullRequest> get(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @PathVariable("pullRequestId") int pullRequestId);


    /**
     * Gets repository pr containing commit.
     *
     * @param project  the project
     * @param repo     the repo
     * @param commitId the commit id
     * @param start    the start
     * @param limit    the limit
     * @return the repository pr containing commit
     */
    @Documentation("https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-pull-requests/#api-api-latest-projects-projectkey-repos-repositoryslug-commits-commitid-pull-requests-get")
    @GetExchange("/{project}/repos/{repo}/commits/{commitId}/pull-requests")
    ResponseEntity<PullRequestPage> getRepositoryPRContainingCommit(@PathVariable("project") String project,
                                                                    @PathVariable("repo") String repo,
                                                                    @PathVariable("commitId") String commitId,
                                                                    @Nullable @RequestParam(required = false, name = "start") Integer start,
                                                                    @Nullable @RequestParam(required = false, name = "limit") Integer limit);

    /**
     * List response entity.
     *
     * @param project        the project
     * @param repo           the repo
     * @param direction      the direction
     * @param branchOrTag    the branch or tag
     * @param state          the state
     * @param order          the order
     * @param withAttributes the with attributes
     * @param withProperties the with properties
     * @param start          the start
     * @param limit          the limit
     * @return the response entity
     */
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


    /**
     * Create response entity.
     *
     * @param project           the project
     * @param repo              the repo
     * @param createPullRequest the create pull request
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/pull-requests")
    ResponseEntity<PullRequest> create(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @RequestBody CreatePullRequest createPullRequest);

    /**
     * Stream raw pull request diff response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param contextLines  the context lines
     * @param whitespace    the whitespace
     * @return the response entity
     */
    @Documentation("https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-pull-requests/#api-api-latest-projects-projectkey-repos-repositoryslug-pull-requests-pullrequestid-patch-get")
    @GetExchange(value = "/{project}/repos/{repo}/pull-requests/{pullRequestId}.diff", accept = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> streamRawPullRequestDiff(@PathVariable("project") String project,
                                                    @PathVariable("repo") String repo,
                                                    @PathVariable("pullRequestId") int pullRequestId,
                                                    @RequestParam(value = "contextLines", required = false) String contextLines,
                                                    @RequestParam(value = "whitespace", required = false) String whitespace);


    /**
     * Edit response entity.
     *
     * @param project         the project
     * @param repo            the repo
     * @param pullRequestId   the pull request id
     * @param editPullRequest the edit pull request
     * @return the response entity
     */
    @PutExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}")
    ResponseEntity<PullRequest> edit(@PathVariable("project") String project,
                                     @PathVariable("repo") String repo,
                                     @PathVariable("pullRequestId") int pullRequestId,
                                     @RequestBody EditPullRequest editPullRequest);


    /**
     * Delete response entity.
     *
     * @param project            the project
     * @param repo               the repo
     * @param pullRequestId      the pull request id
     * @param pullRequestVersion the pull request version
     * @return the response entity
     */
    @DeleteExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("pullRequestId") long pullRequestId,
                                @RequestBody PullRequestVersion pullRequestVersion);


    /**
     * Merge response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param version       the version
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/merge")
    ResponseEntity<PullRequest> merge(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @PathVariable("pullRequestId") int pullRequestId,
                                      @RequestParam("version") int version);


    /**
     * Can merge response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/merge")
    ResponseEntity<MergeStatus> canMerge(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("pullRequestId") int pullRequestId);


    /**
     * Decline response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param version       the version
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/decline")
    ResponseEntity<PullRequest> decline(@PathVariable("project") String project,
                                        @PathVariable("repo") String repo,
                                        @PathVariable("pullRequestId") int pullRequestId,
                                        @RequestParam("version") int version);


    /**
     * Reopen response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param version       the version
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/reopen")
    ResponseEntity<PullRequest> reopen(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("pullRequestId") int pullRequestId,
                                       @RequestParam("version") int version);


    /**
     * Changes response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param withComments  the with comments
     * @param limit         the limit
     * @param start         the start
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/changes")
    ResponseEntity<ChangePage> changes(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("pullRequestId") int pullRequestId,
                                       @Nullable @RequestParam(required = false, name = "withComments") Boolean withComments,
                                       @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                       @Nullable @RequestParam(required = false, name = "start") Integer start);


    /**
     * Commits response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param withCounts    the with counts
     * @param limit         the limit
     * @param start         the start
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/commits")
    ResponseEntity<CommitPage> commits(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("pullRequestId") int pullRequestId,
                                       @Nullable @RequestParam(required = false, name = "withCounts") Boolean withCounts,
                                       @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                       @Nullable @RequestParam(required = false, name = "start") Integer start);


    /**
     * List activities response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param limit         the limit
     * @param start         the start
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/activities")
    ResponseEntity<ActivitiesPage> listActivities(@PathVariable("project") String project,
                                                  @PathVariable("repo") String repo,
                                                  @PathVariable("pullRequestId") long pullRequestId,
                                                  @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                                  @Nullable @RequestParam(required = false, name = "start") Integer start);


    /**
     * List participants response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param limit         the limit
     * @param start         the start
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/participants")
    ResponseEntity<ParticipantsPage> listParticipants(@PathVariable("project") String project,
                                                      @PathVariable("repo") String repo,
                                                      @PathVariable("pullRequestId") long pullRequestId,
                                                      @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                                      @Nullable @RequestParam(required = false, name = "start") Integer start);


    /**
     * Assign participant response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param participants  the participants
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/participants")
    ResponseEntity<Participants> assignParticipant(@PathVariable("project") String project,
                                                   @PathVariable("repo") String repo,
                                                   @PathVariable("pullRequestId") long pullRequestId,
                                                   @RequestBody CreateParticipants participants);


    /**
     * Delete participant response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param userSlug      the user slug
     * @return the response entity
     */
    @DeleteExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/participants/{userSlug}")
    ResponseEntity<Void> deleteParticipant(@PathVariable("project") String project,
                                           @PathVariable("repo") String repo,
                                           @PathVariable("pullRequestId") long pullRequestId,
                                           @PathVariable("userSlug") String userSlug);


    /**
     * Add participant response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param userSlug      the user slug
     * @param participants  the participants
     * @return the response entity
     */
    @PutExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/participants/{userSlug}")
    ResponseEntity<Participants> addParticipant(@PathVariable("project") String project,
                                                @PathVariable("repo") String repo,
                                                @PathVariable("pullRequestId") long pullRequestId,
                                                @PathVariable("userSlug") String userSlug,
                                                @RequestBody CreateParticipants participants);
}
