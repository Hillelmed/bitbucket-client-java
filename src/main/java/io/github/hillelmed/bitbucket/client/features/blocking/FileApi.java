package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.domain.commit.Commit;
import io.github.hillelmed.bitbucket.client.domain.file.FilesPage;
import io.github.hillelmed.bitbucket.client.domain.file.LastModified;
import io.github.hillelmed.bitbucket.client.domain.file.LinePage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;

/**
 * The interface File api.
 */
public interface FileApi {


    /**
     * Raw response entity.
     *
     * @param project     the project
     * @param repo        the repo
     * @param filePath    the file path
     * @param branchOrTag the branch or tag
     * @return the response entity
     */
    @GetExchange(value = "/projects/{project}/repos/{repo}/raw/{filePath}", accept = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> raw(@PathVariable("project") String project,
                               @PathVariable("repo") String repo,
                               @PathVariable("filePath") String filePath,
                               @Nullable @RequestParam(required = false, value = "at") String branchOrTag);


    /**
     * List lines response entity.
     *
     * @param project     the project
     * @param repo        the repo
     * @param filePath    the file path
     * @param branchOrTag the branch or tag
     * @param type        the type
     * @param blame       the blame
     * @param noContent   the no content
     * @param start       the start
     * @param limit       the limit
     * @return the response entity
     */
    @GetExchange("/rest/api/latest/projects/{project}/repos/{repo}/browse/{filePath}")
    ResponseEntity<LinePage> listLines(@PathVariable("project") String project,
                                       @PathVariable("repo") String repo,
                                       @PathVariable("filePath") String filePath,
                                       @Nullable @RequestParam(required = false, value = "at") String branchOrTag,
                                       @Nullable @RequestParam(required = false, value = "type") Boolean type,
                                       @Nullable @RequestParam(required = false, value = "blame") Boolean blame,
                                       @Nullable @RequestParam(required = false, value = "noContent") Boolean noContent,
                                       @Nullable @RequestParam(required = false, value = "start") Integer start,
                                       @Nullable @RequestParam(required = false, value = "limit") Integer limit);


    /**
     * Update content response entity.
     *
     * @param project         the project
     * @param repo            the repo
     * @param filePathEncoded the file path encoded
     * @param branch          the branch
     * @param content         the content
     * @param message         the message
     * @param sourceCommitId  the source commit id
     * @param sourceBranch    the source branch
     * @return the response entity
     */
    @PutExchange("/rest/api/latest/projects/{project}/repos/{repo}/browse/{filePath}")
    ResponseEntity<Commit> updateContent(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("filePath") String filePathEncoded,
                                         @RequestPart("branch") String branch,
                                         @RequestPart(name = "content") String content,
                                         @Nullable @RequestPart(required = false, value = "message") String message,
                                         @Nullable @RequestPart(required = false, value = "sourceCommitId") String sourceCommitId,
                                         @Nullable @RequestPart(required = false, value = "sourceBranch") String sourceBranch);


    /**
     * List files response entity.
     *
     * @param project     the project
     * @param repo        the repo
     * @param path        the path
     * @param branchOrTag the branch or tag
     * @param start       the start
     * @param limit       the limit
     * @return the response entity
     */
    @GetExchange("/rest/api/latest/projects/{project}/repos/{repo}/files/{path}")
    ResponseEntity<FilesPage> listFiles(@PathVariable("project") String project,
                                        @PathVariable("repo") String repo,
                                        @Nullable @PathVariable(value = "path") String path,
                                        @Nullable @RequestParam(required = false, value = "at") String branchOrTag,
                                        @Nullable @RequestParam(required = false, value = "start") Integer start,
                                        @Nullable @RequestParam(required = false, value = "limit") Integer limit);


    /**
     * List files response entity.
     *
     * @param project     the project
     * @param repo        the repo
     * @param branchOrTag the branch or tag
     * @param start       the start
     * @param limit       the limit
     * @return the response entity
     */
    @GetExchange("/rest/api/latest/projects/{project}/repos/{repo}/files")
    ResponseEntity<FilesPage> listFiles(@PathVariable("project") String project,
                                        @PathVariable("repo") String repo,
                                        @Nullable @RequestParam(required = false, value = "at") String branchOrTag,
                                        @Nullable @RequestParam(required = false, value = "start") Integer start,
                                        @Nullable @RequestParam(required = false, value = "limit") Integer limit);


    /**
     * Last modified response entity.
     *
     * @param project     the project
     * @param repo        the repo
     * @param path        the path
     * @param branchOrTag the branch or tag
     * @return the response entity
     */
    @GetExchange(value = "/rest/api/latest/projects/{project}/repos/{repo}/last-modified/{path}")
    ResponseEntity<LastModified> lastModified(@PathVariable("project") String project,
                                              @PathVariable("repo") String repo,
                                              @Nullable @PathVariable(name = "path") String path,
                                              @RequestParam(value = "at") String branchOrTag);


    /**
     * Last modified response entity.
     *
     * @param project     the project
     * @param repo        the repo
     * @param branchOrTag the branch or tag
     * @return the response entity
     */
    @GetExchange(value = "/rest/api/latest/projects/{project}/repos/{repo}/last-modified")
    ResponseEntity<LastModified> lastModified(@PathVariable("project") String project,
                                              @PathVariable("repo") String repo,
                                              @RequestParam(value = "at") String branchOrTag);
}
