package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.annotations.Documentation;
import io.github.hillelmed.bitbucket.client.domain.commit.Commit;
import io.github.hillelmed.bitbucket.client.domain.commit.CommitPage;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.ChangePage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;


/**
 * The interface Commits api.
 */
@HttpExchange(url = "/rest/api/latest/projects", accept = "application/json", contentType = "application/json")
public interface CommitsApi {


    /**
     * Get response entity.
     *
     * @param project  the project
     * @param repo     the repo
     * @param commitId the commit id
     * @param path     the path
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/commits/{commitId}")
    ResponseEntity<Commit> get(@PathVariable("project") String project,
                               @PathVariable("repo") String repo,
                               @PathVariable("commitId") String commitId,
                               @Nullable @RequestParam(required = false, name = "path") String path);

    /**
     * Gets diff.
     *
     * @param project      the project
     * @param repo         the repo
     * @param commitId     the commit id
     * @param contextLines the context lines
     * @param srcPath      the src path
     * @param autoSrcPath  the auto src path
     * @param whitespace   the whitespace
     * @param since        the since
     * @return the diff
     */
    @Documentation("https://developer.atlassian.com/server/bitbucket/rest/v811/api-group-repository/#api-api-latest-projects-projectkey-repos-repositoryslug-commits-commitid-diff-get")
    @GetExchange(value = "/{project}/repos/{repo}/commits/{commitId}/diff", accept = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> getDiff(@PathVariable("project") String project,
                                   @PathVariable("repo") String repo,
                                   @PathVariable("commitId") String commitId,
                                   @Nullable @RequestParam(required = false, name = "contextLines") Integer contextLines,
                                   @Nullable @RequestParam(required = false, name = "srcPath") String srcPath,
                                   @Nullable @RequestParam(required = false, name = "autoSrcPath") Boolean autoSrcPath,
                                   @Nullable @RequestParam(required = false, name = "whitespace") String whitespace,
                                   @Nullable @RequestParam(required = false, name = "since") String since);


    /**
     * List changes response entity.
     *
     * @param project  the project
     * @param repo     the repo
     * @param commitId the commit id
     * @param limit    the limit
     * @param start    the start
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/commits/{commitId}/changes")
    ResponseEntity<ChangePage> listChanges(@PathVariable("project") String project,
                                           @PathVariable("repo") String repo,
                                           @PathVariable("commitId") String commitId,
                                           @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                           @Nullable @RequestParam(required = false, name = "start") Integer start);


    /**
     * List response entity.
     *
     * @param project       the project
     * @param repo          the repo
     * @param withCounts    the with counts
     * @param followRenames the follow renames
     * @param ignoreMissing the ignore missing
     * @param merges        the merges
     * @param path          the path
     * @param since         the since
     * @param until         the until
     * @param limit         the limit
     * @param start         the start
     * @return the response entity
     */
    @GetExchange("/{project}/repos/{repo}/commits")
    ResponseEntity<CommitPage> list(@PathVariable("project") String project,
                                    @PathVariable("repo") String repo,
                                    @Nullable @RequestParam(required = false, name = "withCounts") Boolean withCounts,
                                    @Nullable @RequestParam(required = false, name = "followRenames") Boolean followRenames,
                                    @Nullable @RequestParam(required = false, name = "ignoreMissing") Boolean ignoreMissing,
                                    @Nullable @RequestParam(required = false, name = "merges") String merges,
                                    @Nullable @RequestParam(required = false, name = "path") String path,
                                    @Nullable @RequestParam(required = false, name = "since") String since,
                                    @Nullable @RequestParam(required = false, name = "until") String until,
                                    @Nullable @RequestParam(required = false, name = "limit") Integer limit,
                                    @Nullable @RequestParam(required = false, name = "start") Integer start);
}
