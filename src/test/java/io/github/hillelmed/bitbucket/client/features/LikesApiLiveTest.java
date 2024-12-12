package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.branch.*;
import io.github.hillelmed.bitbucket.client.domain.comment.*;
import io.github.hillelmed.bitbucket.client.domain.common.*;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import io.github.hillelmed.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "LikesApiLiveTest", singleThreaded = true)
public class LikesApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String commentText = TestUtilities.randomString();
    private GeneratedTestContents generatedTestContents;
    private String project;
    private String repo;
    private int prId = -1;
    private int commentId = -1;

    // base setup is identical to CommentsLiveTest
    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        this.project = generatedTestContents.project.getKey();
        this.repo = generatedTestContents.repository.getName();
        final BranchPage branchPage = api.branchApi().list(project, repo, null, null, null, null, null, null).getBody();
        assertThat(branchPage).isNotNull();
        Assertions.assertThat(branchPage.getValues().size()).isEqualTo(2);

        String branchToMerge = null;
        for (final Branch branch : branchPage.getValues()) {
            if (!branch.getId().endsWith("master")) {
                branchToMerge = branch.getId();
                break;
            }
        }
        assertThat(branchToMerge).isNotNull();

        final String randomChars = TestUtilities.randomString();
        final ProjectKey proj = new ProjectKey(project);
        final MinimalRepository repository = new MinimalRepository(repo, null, proj);
        final Reference fromRef = new Reference(branchToMerge, repository, branchToMerge);
        final Reference toRef = new Reference(null, repository);
        final CreatePullRequest cpr = new CreatePullRequest(randomChars, "Fix for issue " + randomChars, fromRef, toRef);
        final PullRequest pr = api.pullRequestApi().create(project, repo, cpr).getBody();

        assertThat(pr).isNotNull();
        assertThat(project).isEqualTo(pr.getFromRef().getRepository().getProject().getKey());
        assertThat(repo).isEqualTo(pr.getFromRef().getRepository().getName());
        prId = pr.getId();

        final Comments comm = api.commentsApi().comment(project, repo, prId, new UpdateComment(commentText)).getBody();
        commentId = comm.getId();
    }

    @Test()
    public void testGetNoLikes() {
        final LikePage likePage = api().getLikes(project, repo, prId, commentId).getBody();
        assertThat(likePage).isNotNull();
        Assertions.assertThat(likePage.getValues()).hasSize(0);
    }

    @Test(dependsOnMethods = "testGetNoLikes")
    public void testCreateLike() {
        final ResponseEntity<Void> requestStatus = api().likeComment(project, repo, prId, commentId);
        assertThat(requestStatus).isNotNull();
        assertThat(requestStatus.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test(dependsOnMethods = "testCreateLike")
    public void testGetOneLike() {
        final LikePage likePage = api().getLikes(project, repo, prId, commentId).getBody();
        assertThat(likePage).isNotNull();
        Assertions.assertThat(likePage.getValues()).hasSize(1);
        Assertions.assertThat(likePage.getValues().get(0)).isNotNull(); // all contents here are based on the user running the action.
    }

    @Test(dependsOnMethods = "testGetOneLike")
    public void testUnlike() {
        final ResponseEntity<Void> requestStatus = api().unlikeComment(project, repo, prId, commentId);
        assertThat(requestStatus).isNotNull();
        assertThat(requestStatus.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test(dependsOnMethods = "testUnlike")
    public void testGetNoLikesAfterUnlike() {
        final LikePage likePage = api().getLikes(project, repo, prId, commentId).getBody();
        assertThat(likePage).isNotNull();
        Assertions.assertThat(likePage.getValues()).isEmpty();
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private LikesApi api() {
        return bitbucketClient.api().likesApi();
    }
}
