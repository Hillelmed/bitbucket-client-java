package io.github.hillelmed.bitbucket.client.features.blocking;


import io.github.hillelmed.bitbucket.client.domain.comment.LikePage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;


/**
 * The interface Likes api.
 */
@HttpExchange(url = "/rest/comment-likes/latest/projects", accept = "application/json", contentType = "application/json")
public interface LikesApi {


    /**
     * Gets likes.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param commentId     the comment id
     * @return the likes
     */
    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}/likes")
    ResponseEntity<LikePage> getLikes(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @PathVariable("pullRequestId") int pullRequestId,
                                      @PathVariable("commentId") int commentId);


    /**
     * Like comment response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param commentId     the comment id
     * @return the response entity
     */
    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}/likes")
    ResponseEntity<Void> likeComment(@PathVariable("project") String project,
                                     @PathVariable("repo") String repo,
                                     @PathVariable("pullRequestId") int pullRequestId,
                                     @PathVariable("commentId") int commentId);


    /**
     * Unlike comment response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param pullRequestId the pull request id
     * @param commentId     the comment id
     * @return the response entity
     */
    @DeleteExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}/likes")
    ResponseEntity<Void> unlikeComment(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("pullRequestId") int pullRequestId,
                                       @PathVariable("commentId") int commentId);

}
