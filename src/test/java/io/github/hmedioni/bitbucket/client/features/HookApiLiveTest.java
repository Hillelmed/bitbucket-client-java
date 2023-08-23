package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.repository.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import org.assertj.core.api.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "HookApiLiveTest", singleThreaded = true)
public class HookApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String testGetRepoKeyword = "testGetRepository";
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;
    private Hook foundHook;


    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();
    }

    @Test
    public void testGetRepository() {
        final Repository repository = bitbucketClient.api().repositoryApi().get(projectKey, repoKey).getBody();
        assertThat(repository).isNotNull();

        assertThat(repoKey.equalsIgnoreCase(repository.getName())).isTrue();
    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testListRepositories() {
        final RepositoryPage repositoryPage = bitbucketClient.api().repositoryApi().list(projectKey, 0, 100).getBody();

        assertThat(repositoryPage).isNotNull();

        Assertions.assertThat(repositoryPage.getSize()).isPositive();

        Assertions.assertThat(repositoryPage.getValues()).isNotEmpty();
        boolean found = false;
        for (final Repository possibleRepo : repositoryPage.getValues()) {
            if (possibleRepo.getName().equals(repoKey)) {
                found = true;
                break;
            }
        }
        assertThat(found).isTrue();
    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testListHooks() {
        final HookPage hookPage = api().list(projectKey, repoKey, 0, 100).getBody();
        assertThat(hookPage).isNotNull();

        Assertions.assertThat(hookPage.getSize()).isPositive();
        for (final Hook hook : hookPage.getValues()) {
            assert hook.getDetails() != null;
            if (hook.getDetails().getConfigFormKey() == null) {
                assertThat(hook.getDetails().getKey()).isNotNull();
                this.foundHook = hook;
                break;
            }
        }
    }

    @Test()
    public void testListHookOnError() {
        try {
            final HookPage hookPage = api().list(projectKey, TestUtilities.randomString(), 0, 100).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = {"testListHooks"})
    public void testGetHook() {
        final Hook hook = api().get(projectKey, repoKey, foundHook.getDetails().getKey()).getBody();
        assertThat(hook).isNotNull();

        assertThat(hook.getDetails().getKey().equals(foundHook.getDetails().getKey())).isTrue();
        assertThat(hook.isEnabled()).isFalse();
    }


    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testGetHookOnError() {
        try {
            final Hook hook = api().get(projectKey,
                    repoKey,
                    TestUtilities.randomStringLettersOnly()
                            + ":"
                            + TestUtilities.randomStringLettersOnly()).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private HookApi api() {
        return bitbucketClient.api().hookApi();
    }
}
