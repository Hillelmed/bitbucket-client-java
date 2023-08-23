package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.repository.*;
import io.github.hmedioni.bitbucket.client.domain.sync.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;


@Test(groups = "live", testName = "SyncApiLiveTest", singleThreaded = true)
public class SyncApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String newRepoKey = TestUtilities.randomStringLettersOnly();
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;
    private String newProjectKey;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(bitbucketClient, bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();
        this.newProjectKey = this.projectKey;

        final Repository repo = api.repositoryApi().fork(projectKey, repoKey, new ForkRepository(newRepoKey, new ProjectKey(projectKey))).getBody();
        assertThat(repo).isNotNull();

        generatedTestContents.addRepoForDeletion(newProjectKey, newRepoKey);
    }

    @Test
    public void testDisableSync() {
        final SyncStatus status = api().enable(newProjectKey, newRepoKey, new CreateSyncStatus(false)).getBody();
        assertThat(status).isNotNull();
        assertThat(status.getAvailable()).isTrue();
        assertThat(status.getEnabled()).isFalse();
    }

    @Test(dependsOnMethods = "testDisableSync")
    public void testEnableSync() {
        final SyncStatus status = api().enable(newProjectKey, newRepoKey, new CreateSyncStatus(true)).getBody();
        assertThat(status).isNotNull();
        assertThat(status.getAvailable()).isTrue();
        assertThat(status.getEnabled()).isTrue();
    }

    @Test(dependsOnMethods = "testEnableSync")
    public void testSynchronzie() {
        try {
            final SyncState status = api().synchronize(newProjectKey, newRepoKey, SyncOptions.discard(null)).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
            Assertions.assertThat(e.errors().get(0).getMessage().contains("cannot be synchronized"));
        }


        // expected as the there is no code in the repo
    }

    @Test(dependsOnMethods = "testSynchronzie")
    public void testGetSyncStatus() {
        final SyncStatus status = api().status(newProjectKey, newRepoKey, null).getBody();
        assertThat(status).isNotNull();
        assertThat(status.getAvailable()).isTrue();
        assertThat(status.getEnabled()).isTrue();
        Assertions.assertThat(status.getAheadRefs()).isEmpty();
        Assertions.assertThat(status.getDivergedRefs()).isEmpty();
        Assertions.assertThat(status.getOrphanedRefs()).isEmpty();
    }

    @Test
    public void testEnableSyncOnError() {
        try {
            final SyncStatus status = api().enable(newProjectKey, TestUtilities.randomStringLettersOnly(), new CreateSyncStatus(true)).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test
    public void testSyncStatusOnError() {
        try {
            final SyncStatus status = api().status(newProjectKey, TestUtilities.randomStringLettersOnly(), null).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test
    public void testSynchronizeOnError() {
        try {
            final SyncState status = api().synchronize(newProjectKey, TestUtilities.randomStringLettersOnly(), SyncOptions.merge(null)).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private SyncApi api() {
        return api.syncApi();
    }
}
