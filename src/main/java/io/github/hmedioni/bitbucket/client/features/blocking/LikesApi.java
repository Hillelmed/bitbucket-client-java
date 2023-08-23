package io.github.hmedioni.bitbucket.client.features.blocking;


import io.github.hmedioni.bitbucket.client.domain.comment.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/comment-likes/latest/projects", accept = "application/json", contentType = "application/json")
public interface LikesApi {


    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}/likes")
    ResponseEntity<LikePage> getLikes(@PathVariable("project") String project,
                                      @PathVariable("repo") String repo,
                                      @PathVariable("pullRequestId") int pullRequestId,
                                      @PathVariable("commentId") int commentId);


    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}/likes")
    ResponseEntity<Void> likeComment(@PathVariable("project") String project,
                                     @PathVariable("repo") String repo,
                                     @PathVariable("pullRequestId") int pullRequestId,
                                     @PathVariable("commentId") int commentId);


    @DeleteExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}/likes")
    ResponseEntity<Void> unlikeComment(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("pullRequestId") int pullRequestId,
                                       @PathVariable("commentId") int commentId);

}
