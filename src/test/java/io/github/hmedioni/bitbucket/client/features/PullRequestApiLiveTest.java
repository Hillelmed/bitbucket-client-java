package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.config.*;
import io.github.hmedioni.bitbucket.client.domain.activities.*;
import io.github.hmedioni.bitbucket.client.domain.admin.*;
import io.github.hmedioni.bitbucket.client.domain.branch.*;
import io.github.hmedioni.bitbucket.client.domain.commit.*;
import io.github.hmedioni.bitbucket.client.domain.common.*;
import io.github.hmedioni.bitbucket.client.domain.participants.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.io.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "PullRequestApiLiveTest", singleThreaded = true)
public class PullRequestApiLiveTest extends BaseBitbucketApiLiveTest {

    private static final String TEST_USER_NAME = "TestUserName2";
    private static final String TEST_USER_PASSWORD = "TestUserPassword2";

    private GeneratedTestContents generatedTestContents;
    private BitbucketApi testUserApi;

    private String project;
    private String repo;
    private String branchToMerge;
    private Participants participants;
    private int prId = -1;
    private int version = -1;

    private void setupGit() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        project = generatedTestContents.project.getKey();
        repo = generatedTestContents.repository.getName();
        final BranchPage branchPage = bitbucketClient.api().branchApi().list(project, repo, null, null, null, null, null, null).getBody();
        assertThat(branchPage).isNotNull();
        Assertions.assertThat(branchPage.getValues().size()).isEqualTo(2);

        for (final Branch branch : branchPage.getValues()) {
            if (!branch.getId().endsWith("master")) {
                this.branchToMerge = branch.getId();
                break;
            }
        }

        assertThat(branchToMerge).isNotNull();
    }

    @BeforeClass
    public void init() {
        setupGit();
        addTestUser();
        testUserApi = apiForTestUser();
    }

    private void createPullRequest() {
        final String randomChars = TestUtilities.randomString();
        final ProjectKey proj = new ProjectKey(project);
        final MinimalRepository repository = new MinimalRepository(repo, null, proj);
        final Reference fromRef = new Reference(branchToMerge, repository, branchToMerge);
        final Reference toRef = new Reference(null, repository);
        final CreatePullRequest cpr = new CreatePullRequest(randomChars, "Fix for issue " + randomChars, fromRef, toRef);
        final PullRequest pr = api().create(project, repo, cpr).getBody();
        assertThat(pr).isNotNull();
        assertThat(project.equals(pr.getFromRef().getRepository().getProject().getKey())).isTrue();
        assertThat(repo.equals(pr.getFromRef().getRepository().getName())).isTrue();
        prId = pr.getId();
        version = pr.getVersion();
    }

    @Test
    public void testCreatePullRequest() {
        createPullRequest();
    }

    @Test(dependsOnMethods = "testCreatePullRequest")
    public void testEditPullRequest() {
        final PullRequest pr = api().get(project, repo, prId).getBody();
        final EditPullRequest epr = new EditPullRequest(prId, pr.getVersion(), pr.getTitle(), pr.getDescription() + " [edited]", pr.getReviewers());
        final PullRequest editedPr = api().edit(project, repo, prId, epr).getBody();
        assertThat(editedPr.getVersion()).isEqualTo(pr.getVersion() + 1);
        version = editedPr.getVersion();
        assertThat(editedPr.getDescription()).endsWith("[edited]");
    }


    @Test(dependsOnMethods = "testCreatePullRequest")
    public void testGetPullRequest() {
        final PullRequest pr = api().get(project, repo, prId).getBody();
        assertThat(pr).isNotNull();
        assertThat(project.equals(pr.getFromRef().getRepository().getProject().getKey())).isTrue();
        assertThat(repo.equals(pr.getFromRef().getRepository().getName())).isTrue();
        assertThat(version == pr.getVersion()).isTrue();
    }

    @Test(dependsOnMethods = "testGetPullRequest")
    public void testListPullRequest() {
        final PullRequestPage pr = api().list(project, repo, null, null, null, null, null, null, null, 10).getBody();
        assertThat(pr).isNotNull();
        assertThat(!pr.getValues().isEmpty()).isTrue();
    }

    @Test(dependsOnMethods = "testListPullRequest")
    public void testGetPullRequestChanges() {
        final ChangePage pr = api().changes(project, repo, prId, null, null, null).getBody();
        assertThat(pr).isNotNull();

        assertThat(!pr.getValues().isEmpty()).isTrue();
    }

    @Test(dependsOnMethods = "testGetPullRequestChanges")
    public void testGetPullRequestCommits() {
        final CommitPage pr = api().commits(project, repo, prId, true, 1, null).getBody();
        assertThat(pr).isNotNull();

        assertThat(pr.getValues().size() == 1).isTrue();
        assertThat(pr.getTotalCount() > 0).isTrue();
    }

    @Test(dependsOnMethods = {"testGetPullRequestCommits", "testAddExistingParticipant"})
    public void testDeclinePullRequest() {
        final PullRequest pr = api().decline(project, repo, prId, version).getBody();
        assertThat(pr).isNotNull();
        assertThat("DECLINED".equalsIgnoreCase(pr.getState())).isTrue();
        assertThat(pr.isOpen()).isFalse();
    }

    @Test(dependsOnMethods = "testDeclinePullRequest")
    public void testReopenPullRequest() {
        PullRequest pr = api().get(project, repo, prId).getBody();
        pr = api().reopen(project, repo, prId, pr.getVersion()).getBody();
        assertThat(pr).isNotNull();
        assertThat("OPEN".equalsIgnoreCase(pr.getState())).isTrue();
        assertThat(pr.isOpen()).isTrue();
    }

    @Test(dependsOnMethods = "testReopenPullRequest")
    public void testCanMergePullRequest() {
        final MergeStatus mergeStatus = api().canMerge(project, repo, prId).getBody();
        assertThat(mergeStatus).isNotNull();
        assertThat(mergeStatus.isCanMerge()).isTrue();
    }

    @Test(dependsOnMethods = "testCanMergePullRequest")
    public void testMergePullRequest() {
        PullRequest pr = api().get(project, repo, prId).getBody();
        pr = api().merge(project, repo, prId, pr.getVersion()).getBody();
        assertThat(pr).isNotNull();
        assertThat("MERGED".equalsIgnoreCase(pr.getState())).isTrue();
        assertThat(pr.isOpen()).isFalse();
    }

    @Test(dependsOnMethods = "testGetPullRequest")
    public void testGetListParticipants() {
        final ParticipantsPage pg = api().listParticipants(project, repo, prId, 100, 0).getBody();
        assertThat(pg).isNotNull();
        Assertions.assertThat(pg.getValues()).isNotEmpty();
        participants = pg.getValues().get(0);
    }

    @Test(dependsOnMethods = "testGetListParticipants")
    public void testAssignDefaultParticipantsOnError() {
        final CreateParticipants createParticipants = new CreateParticipants(participants.getUser(),
                participants.getLastReviewedCommit(), Participants.Role.REVIEWER, participants.isApproved(), participants.getStatus());
        try {
            final Participants localParticipants = api().assignParticipant(project, repo, prId, createParticipants).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test(dependsOnMethods = "testAssignDefaultParticipantsOnError")
    public void testAssignParticipants() {
        final User testUser = getTestUser();
        try {
            final ResponseEntity<Void> userPermissionsStatus = api.repositoryApi().createPermissionsByUser(project, repo, "REPO_READ", testUser.getSlug());
            assertThat(userPermissionsStatus.getStatusCode().is2xxSuccessful()).isTrue();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isEmpty();
        }


        final CreateParticipants createParticipants = new CreateParticipants(testUser,
                participants.getLastReviewedCommit(), Participants.Role.REVIEWER, participants.isApproved(), participants.getStatus());
        final Participants localParticipants = api().assignParticipant(project, repo, prId, createParticipants).getBody();
        assertThat(localParticipants).isNotNull();
    }

    @Test(dependsOnMethods = "testAssignParticipants")
    public void testDeleteParticipant() {
        final ResponseEntity<Void> success = api().deleteParticipant(project, repo, prId, getTestUser().getSlug());
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Test(dependsOnMethods = "testDeleteParticipant")
    public void testDeleteParticipantNonExistent() {
        try {
            final ResponseEntity<Void> success = api().deleteParticipant(project, repo, prId, TestUtilities.randomString());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test(dependsOnMethods = "testDeleteParticipantNonExistent")
    public void testGetListActivities() {

        try {
            final ActivitiesPage ac = api().listActivities(project, repo, prId, 100, 0).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isEmpty();
        }

    }

    @Test
    public void testGetNonExistentPullRequest() {
        try {
            final PullRequest pr = api().get(TestUtilities.randomString(), TestUtilities.randomString(), 999).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();
        }
    }

    @Test(dependsOnMethods = "testGetListActivities")
    public void testAddNonExistentParticipant() {
        final User testUser = getTestUser();

        final CreateParticipants createParticipants = new CreateParticipants(testUser,
                participants.getLastReviewedCommit(),
                Participants.Role.REVIEWER,
                false,
                Participants.Status.NEEDS_WORK);

        final Participants localParticipants =
                testUserApi.pullRequestApi().addParticipant(project, repo, prId, testUser.getSlug(), createParticipants).getBody();

        assertThat(localParticipants).isNotNull();
        assertThat(localParticipants.getStatus()).isEqualByComparingTo(Participants.Status.NEEDS_WORK);
    }

    @Test(dependsOnMethods = "testAddNonExistentParticipant")
    public void testAddExistingParticipant() {
        final User testUser = getTestUser();

        final CreateParticipants createParticipants = new CreateParticipants(testUser,
                participants.getLastReviewedCommit(),
                Participants.Role.REVIEWER,
                true,
                Participants.Status.APPROVED);

        final Participants localParticipants =
                testUserApi.pullRequestApi().addParticipant(project, repo, prId, testUser.getSlug(), createParticipants).getBody();

        assertThat(localParticipants).isNotNull();
        assertThat(localParticipants.getStatus()).isEqualByComparingTo(Participants.Status.APPROVED);
    }

    @Test(dependsOnMethods = "testMergePullRequest")
    public void testDeleteMergedPullRequest() {
        final PullRequest pr = api().get(project, repo, prId).getBody();
        try {
            final ResponseEntity<Void> success = api().delete(project, repo, pr.getId(), new PullRequestVersion(pr.getVersion()));
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @Test(dependsOnMethods = "testDeleteMergedPullRequest")
    public void testDeleteBadVersionOfPullRequest() {
        // Since the PR has been merged by another test, we now need a new PR.
        // To get a new PR, we need to create new branch in git, push it and open a new PR with it.
        terminateGitContents();
        setupGit();
        createPullRequest();
        try {
            final ResponseEntity<Void> success = api().delete(project, repo, prId, new PullRequestVersion(9999));
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test(dependsOnMethods = "testDeleteBadVersionOfPullRequest")
    public void testDeleteNonExistentPullRequest() {
        try {
            final ResponseEntity<Void> success = api().delete(project, repo, 9999, new PullRequestVersion(version));
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test(dependsOnMethods = "testDeleteNonExistentPullRequest")
    public void testDeletePullRequest() {
        final ResponseEntity<Void> success = api().delete(project, repo, prId, new PullRequestVersion(version));
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

    }


    @AfterClass
    public void fin() throws IOException {
        terminateGitContents();
        testUserApi.close();
        deleteTestUser();
    }

    private void terminateGitContents() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private PullRequestApi api() {
        return bitbucketClient.api().pullRequestApi();
    }

    private BitbucketApi apiForTestUser() {
        BitbucketProperties bitbucketProperties1 = new BitbucketProperties(url, TEST_USER_NAME, TEST_USER_PASSWORD);
        return BitbucketClient.create(bitbucketProperties1).api();
    }

    private User getTestUser() {
        final UserPage userPage = bitbucketClient.api().adminApi().listUsers(TEST_USER_NAME, null, null).getBody();
        assertThat(userPage.getValues().isEmpty()).isFalse();
        return userPage.getValues().get(0);
    }

    private void addTestUser() {
        bitbucketClient.api().adminApi().createUser(TEST_USER_NAME, TEST_USER_PASSWORD, TEST_USER_NAME, "test@test.test", true, null);
    }

    private void deleteTestUser() {
        bitbucketClient.api().adminApi().deleteUser(TEST_USER_NAME);
    }
}
