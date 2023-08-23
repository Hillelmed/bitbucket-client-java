package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.commit.*;
import io.github.hmedioni.bitbucket.client.domain.file.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

public interface FileApi {


    @GetExchange(value = "/projects/{project}/repos/{repo}/raw/{filePath}", accept = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> raw(@PathVariable("project") String project,
                               @PathVariable("repo") String repo,
                               @PathVariable("filePath") String filePath,
                               @Nullable @RequestParam(required = false, value = "at") String branchOrTag);


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


    @PutExchange("/rest/api/latest/projects/{project}/repos/{repo}/browse/{filePath}")
    ResponseEntity<Commit> updateContent(@PathVariable("project") String project,
                                         @PathVariable("repo") String repo,
                                         @PathVariable("filePath") String filePathEncoded,
                                         @RequestPart("branch") String branch,
                                         @RequestPart(name = "content") String content,
                                         @Nullable @RequestPart(required = false, value = "message") String message,
                                         @Nullable @RequestPart(required = false, value = "sourceCommitId") String sourceCommitId,
                                         @Nullable @RequestPart(required = false, value = "sourceBranch") String sourceBranch);


    @GetExchange("/rest/api/latest/projects/{project}/repos/{repo}/files/{path}")
    ResponseEntity<FilesPage> listFiles(@PathVariable("project") String project,
                                        @PathVariable("repo") String repo,
                                        @Nullable @PathVariable(value = "path") String path,
                                        @Nullable @RequestParam(required = false, value = "at") String branchOrTag,
                                        @Nullable @RequestParam(required = false, value = "start") Integer start,
                                        @Nullable @RequestParam(required = false, value = "limit") Integer limit);


    @GetExchange("/rest/api/latest/projects/{project}/repos/{repo}/files")
    ResponseEntity<FilesPage> listFiles(@PathVariable("project") String project,
                                        @PathVariable("repo") String repo,
                                        @Nullable @RequestParam(required = false, value = "at") String branchOrTag,
                                        @Nullable @RequestParam(required = false, value = "start") Integer start,
                                        @Nullable @RequestParam(required = false, value = "limit") Integer limit);


    @GetExchange(value = "/rest/api/latest/projects/{project}/repos/{repo}/last-modified/{path}")
    ResponseEntity<LastModified> lastModified(@PathVariable("project") String project,
                                              @PathVariable("repo") String repo,
                                              @Nullable @PathVariable(name = "path") String path,
                                              @RequestParam(value = "at") String branchOrTag);


    @GetExchange(value = "/rest/api/latest/projects/{project}/repos/{repo}/last-modified")
    ResponseEntity<LastModified> lastModified(@PathVariable("project") String project,
                                              @PathVariable("repo") String repo,
                                              @RequestParam(value = "at") String branchOrTag);
}
