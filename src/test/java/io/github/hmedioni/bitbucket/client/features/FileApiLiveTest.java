package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.branch.*;
import io.github.hmedioni.bitbucket.client.domain.commit.*;
import io.github.hmedioni.bitbucket.client.domain.file.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "FileApiLiveTest", singleThreaded = true)
public class FileApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String branch = "refs/heads/master";
    private final String readmeFilePath = "README.md";
    private final String message = "Create README.md";
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;
    private String commitHashOne;
    private String commitHashTwo;
    private String filePath;
    private String content;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(this.bitbucketClient, this.bitbucketProperties);
        projectKey = generatedTestContents.project.getKey();
        repoKey = generatedTestContents.repository.getName();

        final CommitPage commitPage = this.bitbucketClient.api().commitsApi().list(projectKey, repoKey, true, null, null, null, null, null, null, 10, null).getBody();
        assertThat(commitPage).isNotNull();
        Assertions.assertThat(commitPage.getValues().isEmpty()).isFalse();
        assertThat(commitPage.getTotalCount() > 0).isTrue();
        this.commitHashOne = commitPage.getValues().get(0).getId();
        this.commitHashTwo = commitPage.getValues().get(1).getId();

        final ChangePage commit = this.bitbucketClient.api().commitsApi().listChanges(projectKey, repoKey, commitHashOne, 100, 0).getBody();
        assertThat(commit).isNotNull();
        assertThat(commit.getSize() > 0).isTrue();
        this.filePath = commit.getValues().get(0).getPath().toString();
    }

    @Test
    public void getContent() {
        this.content = api().raw(projectKey, repoKey, filePath, commitHashOne).getBody();
        assertThat(content).isNotNull();
    }

    @Test
    public void getContentOnNotFound() {
        try {
            api().raw(projectKey, repoKey, TestUtilities.randomString() + ".txt", null).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.code().value()).isEqualTo(404);
            assertThat(e.errors().isEmpty()).isFalse();
        }
    }

    @Test(dependsOnMethods = "getContent")
    public void listLines() throws Exception {
        final List<Line> allLines = new ArrayList<>();
        Integer start = null;
        while (true) {
            final LinePage linePage = api().listLines(projectKey, repoKey, this.filePath, null, null, null, null, start, 100).getBody();
            allLines.addAll(linePage.getLines());
            start = linePage.getNextPageStart();
            if (linePage.isLastPage()) {
                break;
            } else {
                System.out.println("Sleeping for 1 seconds before querying for next page");
                Thread.sleep(1000);
            }
        }
        assertThat(allLines.size() > 0).isEqualTo(true);
        final StringBuilder possibleContent = new StringBuilder();
        for (final Line possibleLine : allLines) {
            possibleContent.append(possibleLine.getText().trim());
        }
        assertThat(possibleContent.toString().trim()).isEqualTo(this.content.trim());
    }

    @Test(dependsOnMethods = "getContent")
    public void listLinesAtCommit() throws Exception {
        final List<Line> allLines = new ArrayList<>();
        Integer start = null;
        while (true) {
            final LinePage linePage = api().listLines(projectKey, repoKey, this.filePath, this.commitHashTwo, null, true, null, start, 100).getBody();
            assertThat(linePage.getBlame().isEmpty()).isFalse();

            allLines.addAll(linePage.getLines());
            start = linePage.getNextPageStart();
            if (linePage.isLastPage()) {
                break;
            } else {
                System.out.println("Sleeping for 1 seconds before querying for next page");
                Thread.sleep(1000);
            }
        }
        assertThat(allLines.size() > 0).isEqualTo(true);
        final StringBuilder possibleContent = new StringBuilder();
        for (final Line possibleLine : allLines) {
            possibleContent.append(possibleLine.getText());
        }
        assertThat(possibleContent.toString()).isNotEqualTo(this.content.trim());
    }

    @Test
    public void listLinesOnError() {
        try {
            final LinePage linePage = api().listLines(projectKey, repoKey, TestUtilities.randomString() + ".txt", null, null, null, null, null, 100).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();

        }
    }

    @Test(dependsOnMethods = "getContent")
    public void listFilesAtCommit() throws Exception {
        final List<String> allFiles = new ArrayList<>();
        Integer start = null;
        while (true) {
            final FilesPage ref = api().listFiles(projectKey, repoKey, this.commitHashTwo, start, 100).getBody();

            allFiles.addAll(ref.getValues());
            start = ref.getNextPageStart();
            if (ref.isLastPage()) {
                break;
            } else {
                System.out.println("Sleeping for 1 seconds before querying for next page");
                Thread.sleep(1000);
            }
        }
        assertThat(allFiles.size() > 0).isEqualTo(true);
    }

    @Test
    public void listFilesAtPath() {
        final String newDirectory = "listFilesAtPath";
        this.bitbucketClient.api().fileApi().updateContent(projectKey, repoKey, newDirectory + "/" + readmeFilePath, branch, TestUtilities.randomString(), message, null, null);
        final FilesPage files = this.bitbucketClient.api().fileApi().listFiles(projectKey, repoKey, newDirectory, null, null, null).getBody();

        assertThat(files).isNotNull();
        Assertions.assertThat(files.getValues()).isNotNull();
        Assertions.assertThat(files.getValues().get(0)).isEqualTo(readmeFilePath);
    }

    @Test
    public void listFilesOnError() {
        try {
            final FilesPage ref = api().listFiles(projectKey, repoKey, null, TestUtilities.randomString(), null, 100).getBody();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Missing path variable value 'path'");
        }
    }

    @Test
    public void updateContentCreateFile() {
        final String fileContent = UUID.randomUUID().toString();
        final Commit commit = this.bitbucketClient.api().fileApi().updateContent(projectKey, repoKey, readmeFilePath, branch, fileContent, message, null, null).getBody();

        assertThat(commit).isNotNull();
        assertThat(commit.getMessage()).isEqualTo(message);
        assertThat(commit.getId()).isNotEmpty();

        final String newFile = api().raw(projectKey, repoKey, readmeFilePath, commit.getId()).getBody();
        assertThat(newFile).isNotNull();
        assertThat(newFile).isEqualTo(fileContent);
    }

    @Test
    public void updateContentCreateFileOnNewBranch() {
        final String fileContent = UUID.randomUUID().toString();
        final String newBranchName = UUID.randomUUID().toString();
        final String newFilePath = UUID.randomUUID().toString();
        final Commit commit = this.bitbucketClient.api().fileApi().updateContent(projectKey, repoKey, newFilePath, newBranchName, fileContent, message, null, branch).getBody();

        assertThat(commit).isNotNull();
        assertThat(commit.getMessage()).isEqualTo(message);
        assertThat(commit.getId()).isNotEmpty();

        final String newFile = api().raw(projectKey, repoKey, newFilePath, commit.getId()).getBody();
        assertThat(newFile).isNotNull();
        assertThat(newFile).isEqualTo(fileContent);

        final BranchPage branches = this.bitbucketClient.api().branchApi().list(projectKey, repoKey, null, null, newBranchName, null, null, null).getBody();
        assertThat(branches).isNotNull();
        Assertions.assertThat(branches.getValues().size()).isEqualTo(1);
    }

    @Test(dependsOnMethods = "updateContentCreateFile")
    public void updateContentCreateGivenFileAlreadyExists() {
        final String fileContent = UUID.randomUUID().toString();
        try {
            final Commit commit = api().updateContent(projectKey, repoKey, readmeFilePath, branch, fileContent, message, null, null).getBody();
            assertThat(commit).isNotNull();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();
        }
    }

    @Test(dependsOnMethods = "updateContentCreateFile")
    public void updateContent() {
        final String fileContent = UUID.randomUUID().toString();
        final CommitPage commits = bitbucketClient.api().commitsApi().list(projectKey, repoKey, null, null, null, null, null, null, branch, 1, null).getBody();
        final String sourceCommitId = commits.getValues().get(0).getId();
        final Commit commit = api().updateContent(projectKey, repoKey, readmeFilePath, branch, fileContent, null, sourceCommitId, null).getBody();

        assertThat(commit).isNotNull();
        assertThat(commit.getId()).isNotEmpty();

        final String newFile = api().raw(projectKey, repoKey, readmeFilePath, commit.getId()).getBody();
        assertThat(newFile).isNotNull();
        assertThat(newFile).isEqualTo(fileContent);
    }

    @Test(dependsOnMethods = "updateContentCreateFile")
    public void lastModified() {
        final CommitPage commits = bitbucketClient.api().commitsApi().list(projectKey, repoKey, null, null, null, null, null, null, null, 1, null).getBody();
        final Commit commit = commits.getValues().get(0);
        final LastModified summary = bitbucketClient.api().fileApi().lastModified(projectKey, repoKey, branch).getBody();

        assertThat(summary).isNotNull();
        Assertions.assertThat(summary.getLatestCommit()).isNotNull();
        Assertions.assertThat(summary.getLatestCommit().getId()).isEqualTo(commit.getId());
        Assertions.assertThat(summary.getFiles()).isNotEmpty();
        assertThat(summary.getFiles().containsKey(readmeFilePath)).isTrue();
    }

    @Test
    public void lastModifiedAtPath() {
        final String newDirectory = "newDirectory";
        final String fileContent = UUID.randomUUID().toString();
        final Commit commit = bitbucketClient.api().fileApi().updateContent(projectKey, repoKey, newDirectory + "/" + readmeFilePath, branch, fileContent, message, null, null).getBody();
        final LastModified summary = bitbucketClient.api().fileApi().lastModified(projectKey, repoKey, newDirectory, branch).getBody();

        assertThat(summary).isNotNull();
        Assertions.assertThat(summary.getLatestCommit()).isNotNull();
        Assertions.assertThat(summary.getLatestCommit().getId()).isEqualTo(commit.getId());
        Assertions.assertThat(summary.getFiles()).isNotEmpty();
        assertThat(summary.getFiles().containsKey(readmeFilePath)).isTrue();
        Assertions.assertThat(summary.getFiles().get(readmeFilePath).getId()).isEqualTo(commit.getId());
    }

    @Test
    public void lastModifiedGivenEmptyRepository() {
        final String emptyRepository = "lastModifiedGivenEmptyRepository";
        bitbucketClient.api().repositoryApi().create(projectKey, new CreateRepository(emptyRepository, null, null, false));
        try {
            final LastModified summary = bitbucketClient.api().fileApi().lastModified(projectKey, emptyRepository, branch).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();
        }
        generatedTestContents.addRepoForDeletion(projectKey, emptyRepository);
    }

    @Test
    public void lastModifiedAtPathGivenInvalidPath() {
        try {
            final LastModified summary = bitbucketClient.api().fileApi().lastModified(projectKey, repoKey, readmeFilePath, branch).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();
        }
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(this.bitbucketClient.api(), generatedTestContents);
    }

    private FileApi api() {
        return this.bitbucketClient.api().fileApi();
    }
}
