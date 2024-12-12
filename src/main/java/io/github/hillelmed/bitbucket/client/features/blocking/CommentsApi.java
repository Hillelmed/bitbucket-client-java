package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.domain.comment.Comments;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.CommentPage;
import io.github.hillelmed.bitbucket.client.options.CreateComment;
import io.github.hillelmed.bitbucket.client.options.UpdateComment;
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
 * The interface Comments api.
 */
@HttpExchange(url = "/rest/api/latest/projects", accept = "application/json", contentType = "application/json")
public interface CommentsApi {


    /**
     * Comment response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param comment       the comment
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments")
    ResponseEntity<Comments> comment(@PathVariable("project") String project,
                                     @PathVariable("repo") String repo,
                                     @PathVariable("pullRequestId") int pullRequestId,
                                     @RequestBody UpdateComment comment);


    /**
     * Create response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param createComment the create comment
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments")
    ResponseEntity<Comments> create(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @PathVariable("pullRequestId") int pullRequestId,
                                    @RequestBody CreateComment createComment);


    /**
     * Get response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param commentId     the comment id
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}")
    ResponseEntity<Comments> get(@PathVariable("project") String project,
                                 @PathVariable("repo") String repo,
                                 @PathVariable("pullRequestId") int pullRequestId,
                                 @PathVariable("commentId") int commentId);


    /**
     * File comments response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param pathToFile    the path to file
     * @param anchorState   the anchor state
     * @param diffType      the diff type
     * @param fromHash      the from hash
     * @param toHash        the to hash
     * @param start         the start
     * @param limit         the limit
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments")
    ResponseEntity<CommentPage> fileComments(@PathVariable("project") String project,
                                             @PathVariable("repo") String repo,
                                             @PathVariable("pullRequestId") int pullRequestId,
                                             @RequestParam("path") String pathToFile,
                                             @Nullable @RequestParam(required = false, name = "anchorState") String anchorState,
                                             @Nullable @RequestParam(required = false, name = "diffType") String diffType,
                                             @Nullable @RequestParam(required = false, name = "fromHash") String fromHash,
                                             @Nullable @RequestParam(required = false, name = "toHash") String toHash,
                                             @Nullable @RequestParam(required = false, name = "start") Integer start,
                                             @Nullable @RequestParam(required = false, name = "limit") Integer limit);


    /**
     * Delete response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param commentId     the comment id
     * @param version       the version
     * @return the response entity
     */
    @DeleteExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("pullRequestId") int pullRequestId,
                                @PathVariable("commentId") int commentId,
                                @RequestParam("version") int version);
}
