package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.build.*;
import io.github.hillelmed.bitbucket.client.domain.commit.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import io.github.hillelmed.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "BuildStatusApiLiveTest")
public class BuildStatusApiLiveTest extends BaseBitbucketApiLiveTest {

    private final CreateBuildStatus.STATE state = CreateBuildStatus.STATE.SUCCESSFUL;
    private final String key = "REPO-MASTER";
    private final String name = "REPO-MASTER-42";
    private final String url = "https://bamboo.example.com/browse/REPO-MASTER-42";
    private final String description = "Changes by John Doe";
    private GeneratedTestContents generatedTestContents;
    private String commitHash;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        final String projectKey = generatedTestContents.project.getKey();
        final String repoKey = generatedTestContents.repository.getName();

        final CommitPage commitPage = bitbucketClient.api().commitsApi().list(projectKey, repoKey, true, null, null, null, null, null, null, 1, null).getBody();
        assertThat(commitPage).isNotNull();
        Assertions.assertThat(commitPage.getValues().isEmpty()).isFalse();
        assertThat(commitPage.getTotalCount() > 0).isTrue();
        this.commitHash = commitPage.getValues().get(0).getId();
    }

    @Test
    public void testAddStatusToCommit() {
        final CreateBuildStatus cbs = new CreateBuildStatus(state,
                key,
                name,
                url,
                description);
        final ResponseEntity<Void> success = api().add(commitHash, cbs);
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test(dependsOnMethods = "testAddStatusToCommit")
    public void testGetStatusByCommit() {

        final StatusPage statusPage = api().status(commitHash, 0, 100).getBody();
        assertThat(statusPage).isNotNull();
        assertThat(statusPage.getSize() == 1).isTrue();

        final Status status = statusPage.getValues().get(0);
        assertThat(status.getState().toString()).isEqualTo(state.toString());
        assertThat(status.getKey()).isEqualTo(key);
        assertThat(status.getName()).isEqualTo(name);
        assertThat(status.getUrl()).isEqualTo(url);
        assertThat(status.getDescription()).isEqualTo(description);
    }

    @Test
    public void testGetStatusByNonExistentCommit() {

        final StatusPage statusPage = api().status(TestUtilities.randomString(), 0, 100).getBody();
        assertThat(statusPage).isNotNull();
        assertThat(statusPage.getSize() == 0).isTrue();
    }

    @Test(dependsOnMethods = "testGetStatusByCommit")
    public void testGetSummaryByCommit() {

        final Summary summary = api().summary(commitHash).getBody();
        assertThat(summary).isNotNull();
        assertThat(summary.getSuccessful() == 1).isTrue();
        assertThat(summary.getInProgress() == 0).isTrue();
        assertThat(summary.getFailed() == 0).isTrue();
    }

    @Test
    public void testGetSummaryByNonExistentCommit() {

        final Summary summary = api().summary(TestUtilities.randomString()).getBody();
        assertThat(summary).isNotNull();
        assertThat(summary.getSuccessful() == 0).isTrue();
        assertThat(summary.getInProgress() == 0).isTrue();
        assertThat(summary.getFailed() == 0).isTrue();
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(this.bitbucketClient.api(), generatedTestContents);
    }

    private BuildStatusApi api() {
        return this.bitbucketClient.api().buildStatusApi();
    }
}
