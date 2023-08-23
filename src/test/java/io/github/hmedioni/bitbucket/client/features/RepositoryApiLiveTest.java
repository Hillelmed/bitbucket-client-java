package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.domain.repository.ProjectKey;
import io.github.hmedioni.bitbucket.client.domain.repository.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "RepositoryApiLiveTest", singleThreaded = true)
public class RepositoryApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String testGetRepoKeyword = "testGetRepository";
    private final String repoWriteKeyword = "REPO_WRITE";
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;
    private User user;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(this.bitbucketClient, bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();
        this.user = TestUtilities.getDefaultUser(bitbucketClient.getBitbucketAuthentication(), bitbucketClient.api());
    }

    @Test
    public void testGetRepository() {
        final Repository repository = api().get(projectKey, repoKey).getBody();
        assertThat(repository).isNotNull();

        assertThat(repoKey.equalsIgnoreCase(repository.getName())).isTrue();
    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testForkRepository() {
        final String forkedRepoName = TestUtilities.randomStringLettersOnly();
        final Repository repository = api().fork(projectKey, repoKey, new ForkRepository(forkedRepoName, new io.github.hmedioni.bitbucket.client.domain.repository.ProjectKey(projectKey))).getBody();
        assertThat(repository).isNotNull();
        assertThat(forkedRepoName.equalsIgnoreCase(repository.getName())).isTrue();
        generatedTestContents.addRepoForDeletion(projectKey, forkedRepoName);
    }

    @Test
    public void testForkRepositoryNonExistent() {
        try {
            final Repository repository = api().fork(projectKey, TestUtilities.randomStringLettersOnly(), new ForkRepository(TestUtilities.randomStringLettersOnly(), new ProjectKey(projectKey))).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testListRepositories() {
        final RepositoryPage repositoryPage = api().list(projectKey, 0, 100).getBody();

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

    @Test
    public void testListAllRepositories() {

        final List<Repository> foundRepos = new ArrayList<>();

        int start = 0;
        final int limit = 100;
        RepositoryPage repositoryPage;
        do {
            repositoryPage = api().listAll(null, null, null, null, start, limit).getBody();
            start += limit;

            assertThat(repositoryPage).isNotNull();

            Assertions.assertThat(repositoryPage.getSize()).isPositive();

            foundRepos.addAll(repositoryPage.getValues());
        } while (!repositoryPage.isLastPage());

        assertThat(foundRepos).isNotEmpty();

        boolean found = false;
        for (final Repository possibleRepo : foundRepos) {
            if (possibleRepo.getName().equals(repoKey)) {
                found = true;
                break;
            }
        }
        assertThat(found).isTrue();
    }

    @Test
    public void testListAllRepositoriesByRepository() {
        final List<Repository> foundRepos = new ArrayList<>();

        int start = 0;
        final int limit = 100;
        RepositoryPage repositoryPage;
        do {
            repositoryPage = api().listAll(null, repoKey, null, null, start, limit).getBody();
            start += limit;

            assertThat(repositoryPage).isNotNull();

            Assertions.assertThat(repositoryPage.getSize()).isPositive();

            foundRepos.addAll(repositoryPage.getValues());
        } while (!repositoryPage.isLastPage());

        assertThat(foundRepos).isNotEmpty();
        assertThat(foundRepos.size()).isEqualTo(1);
        assertThat(foundRepos.get(0).getName()).isEqualTo(repoKey);
    }

    @Test
    public void testListAllRepositoriesByProjectNonExistent() {
        final String projectKey = "HelloWorld";
        final RepositoryPage repositoryPage = api().listAll(projectKey, null, null, null, 0, 100).getBody();
        assertThat(repositoryPage).isNotNull();

        Assertions.assertThat(repositoryPage.getSize()).isEqualTo(0);
        Assertions.assertThat(repositoryPage.getValues()).isEmpty();
    }

    @Test
    public void testListAllRepositoriesByRepositoryNonExistent() {
        final String repoKey = "HelloWorld";
        final RepositoryPage repositoryPage = api().listAll(null, repoKey, null, null, 0, 100).getBody();
        assertThat(repositoryPage).isNotNull();

        Assertions.assertThat(repositoryPage.getSize()).isEqualTo(0);
        Assertions.assertThat(repositoryPage.getValues()).isEmpty();
    }

    @Test
    public void testDeleteRepositoryNonExistent() {
        final String random = TestUtilities.randomStringLettersOnly();
        try {
            final ResponseEntity<Void> success = api().delete(projectKey, random);
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test
    public void testGetRepositoryNonExistent() {
        try {
            final Repository repository = api().get(projectKey, TestUtilities.randomStringLettersOnly()).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();
        }
    }

    @Test
    public void testCreateRepositoryWithIllegalName() {
        final CreateRepository createRepository = new CreateRepository("!-_999-9*", null, null, true);
        try {
            final Repository repository = api().create(projectKey, createRepository).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();
        }
    }

    @Test(dependsOnMethods = testGetRepoKeyword)
    public void testUpdatePullRequestSettings() {
        final MergeStrategy strategy = new MergeStrategy(null, null, null, MergeStrategy.MergeStrategyId.SQUASH, null);
        final List<MergeStrategy> listStrategy = new ArrayList<>();
        listStrategy.add(strategy);
        final MergeConfig mergeConfig = new MergeConfig(strategy, listStrategy, MergeConfig.MergeConfigType.REPOSITORY, 20);
        final CreatePullRequestSettings pullRequestSettings = new CreatePullRequestSettings(mergeConfig, false, false, 0, 1, true);

        final PullRequestSettings settings = api().updatePullRequestSettings(projectKey, repoKey, pullRequestSettings).getBody();
        assertThat(settings).isNotNull();

        assertThat(settings.getMergeConfig().getStrategies()).isNotEmpty();
        assertThat(MergeStrategy.MergeStrategyId.SQUASH.equals(settings.getMergeConfig().getDefaultStrategy().getId()));
    }

    @Test(dependsOnMethods = "testUpdatePullRequestSettings")
    public void testGetPullRequestSettings() {
        final PullRequestSettings settings = api().getPullRequestSettings(projectKey, repoKey);
        assertThat(settings).isNotNull();

        assertThat(settings.getMergeConfig().getStrategies()).isNotEmpty();
    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testCreatePermissionByUser() {
        final ResponseEntity<Void> success = api().createPermissionsByUser(projectKey, repoKey, repoWriteKeyword, user.getSlug());
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = "testCreatePermissionByUser")
    public void testListPermissionByUser() {
        final PermissionsPage permissionsPage = api().listPermissionsByUser(projectKey, repoKey, 0, 100).getBody();
        Assertions.assertThat(permissionsPage.getValues()).isNotEmpty();
    }

    @Test(dependsOnMethods = {"testListPermissionByUser"})
    public void testDeletePermissionByUser() {
        final ResponseEntity<Void> success = api().deletePermissionsByUser(projectKey, repoKey, user.getSlug());
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testCreatePermissionByGroup() {
        final ResponseEntity<Void> success = api().createPermissionsByGroup(projectKey, repoKey, repoWriteKeyword, defaultBitbucketGroup);
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = "testCreatePermissionByGroup")
    public void testListPermissionByGroup() {
        final PermissionsPage permissionsPage = api().listPermissionsByGroup(projectKey, repoKey, 0, 100).getBody();
        Assertions.assertThat(permissionsPage.getValues()).isNotEmpty();
    }

    @Test(dependsOnMethods = {"testListPermissionByGroup"})
    public void testDeletePermissionByGroup() {
        final ResponseEntity<Void> success = api().deletePermissionsByGroup(projectKey, repoKey, defaultBitbucketGroup);
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testCreatePermissionByGroupNonExistent() {
        try {
            final ResponseEntity<Void> success = api().createPermissionsByGroup(projectKey, repoKey, repoWriteKeyword, TestUtilities.randomString());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testDeletePermissionByGroupNonExistent() {
        final ResponseEntity<Void> success = api().deletePermissionsByGroup(projectKey, repoKey, TestUtilities.randomString());
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue(); // Currently Bitbucket returns the same response if delete is success or not

    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testCreatePermissionByUserNonExistent() {
        try {
            final ResponseEntity<Void> success = api().createPermissionsByUser(projectKey, repoKey, repoWriteKeyword, TestUtilities.randomString());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = {testGetRepoKeyword})
    public void testDeletePermissionByUserNonExistent() {
        try {
            final ResponseEntity<Void> success = api().deletePermissionsByUser(projectKey, repoKey, TestUtilities.randomString());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private RepositoryApi api() {
        return bitbucketClient.api().repositoryApi();
    }
}
