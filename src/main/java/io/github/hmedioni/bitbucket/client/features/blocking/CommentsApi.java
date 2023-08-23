package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.comment.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/api/latest/projects", accept = "application/json", contentType = "application/json")
public interface CommentsApi {


    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments")
    ResponseEntity<Comments> comment(@PathVariable("project") String project,
                                     @PathVariable("repo") String repo,
                                     @PathVariable("pullRequestId") int pullRequestId,
                                     @RequestBody UpdateComment comment);


    @PostExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments")
    ResponseEntity<Comments> create(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @PathVariable("pullRequestId") int pullRequestId,
                                    @RequestBody CreateComment createComment);


    @GetExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}")
    ResponseEntity<Comments> get(@PathVariable("project") String project,
                                 @PathVariable("repo") String repo,
                                 @PathVariable("pullRequestId") int pullRequestId,
                                 @PathVariable("commentId") int commentId);


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


    @DeleteExchange("/{project}/repos/{repo}/pull-requests/{pullRequestId}/comments/{commentId}")
    ResponseEntity<Void> delete(@PathVariable("project") String project,
                                @PathVariable("repo") String repo,
                                @PathVariable("pullRequestId") int pullRequestId,
                                @PathVariable("commentId") int commentId,
                                @RequestParam("version") int version);
}
