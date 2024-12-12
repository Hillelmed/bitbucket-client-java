package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.commit.*;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.*;
import io.github.hillelmed.bitbucket.client.exception.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "CommitsApiLiveTest", singleThreaded = true)
public class CommitsApiLiveTest extends BaseBitbucketApiLiveTest {

    private GeneratedTestContents generatedTestContents;

    private String projectKey;
    private String repoKey;
    private String commitHash;
    private CommitPage commitPage;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();
    }

    @Test
    public void testListCommits() {
        commitPage = api().list(projectKey, repoKey, true, null, null, null, null, null, null, 3, null).getBody();
        assertThat(commitPage).isNotNull();

        Assertions.assertThat(commitPage.getValues().isEmpty()).isFalse();
        assertThat(commitPage.getTotalCount() > 0).isTrue();
        this.commitHash = commitPage.getValues().get(0).getId();
    }

    @Test(dependsOnMethods = "testListCommits")
    public void testListCommitsBetweenCommitHashes() {
        final String startHash = commitPage.getValues().get(2).getId();
        final String endHash = commitPage.getValues().get(0).getId();
        final CommitPage page = api().list(projectKey, repoKey, null, null, null, null, null, startHash, endHash, null, null).getBody();
        assertThat(page).isNotNull();
        assertThat((page.getValues().size() == 2)).isTrue();
        Assertions.assertThat(page.getValues().get(1).getId().equals(commitPage.getValues().get(1).getId())).isTrue();
    }

    @Test
    public void testListCommitsOnError() {
        try {
            final CommitPage pr = api().list(projectKey, TestUtilities.randomStringLettersOnly(), true, null, null, null, null, null, null, 1, null).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();

        }
    }

    @Test(dependsOnMethods = "testListCommits")
    public void testGetCommit() {
        final Commit commit = api().get(projectKey, repoKey, commitHash, null).getBody();
        assertThat(commit).isNotNull();
        assertThat(commit.getId().equals(commitHash)).isTrue();
    }

    @Test(dependsOnMethods = "testGetCommit")
    public void testGetDiff() {
        final ResponseEntity<String> responseEntity = api().getDiff(projectKey, repoKey, commitHash, null,null,null,null,null);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    public void testGetCommitNonExistent() {
        try {
            final Commit commit = api().get(projectKey, repoKey, "1234567890", null).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().size() > 0).isTrue();
        }

    }

    @Test(dependsOnMethods = "testListCommits")
    public void testGetCommitChanges() {
        final ChangePage commit = api().listChanges(projectKey, repoKey, commitHash, 100, 0).getBody();
        assertThat(commit).isNotNull();

        assertThat(commit.getSize() > 0).isTrue();
    }

    @Test
    public void testGetCommitChangesNonExistent() {
        try {
            final ChangePage commit = api().listChanges(projectKey, repoKey, "1234567890", 100, 0).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().size() > 0).isTrue();

        }
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private CommitsApi api() {
        return bitbucketClient.api().commitsApi();
    }
}
