package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.branch.*;
import io.github.hmedioni.bitbucket.client.domain.comment.*;
import io.github.hmedioni.bitbucket.client.domain.common.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "CommentsApiLiveTest", singleThreaded = true)
public class CommentsApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String commentText = TestUtilities.randomString();
    private final String commentReplyText = TestUtilities.randomString();
    private GeneratedTestContents generatedTestContents;
    private String project;
    private String repo;
    private String filePath;
    private int prId = -1;
    private int commentId = -1;
    private int commentReplyId = -1;
    private int commentReplyIdVersion = -1;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        project = generatedTestContents.project.getKey();
        repo = generatedTestContents.repository.getName();
        final BranchPage branchPage = bitbucketClient.api().branchApi().list(project, repo, null, null, null, null, null, null).getBody();
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
        final Reference toRef = new Reference(null, repository, null);
        final CreatePullRequest cpr = new CreatePullRequest(randomChars,
                "Fix for issue " + randomChars, fromRef, toRef);
        final PullRequest pr = bitbucketClient.api().pullRequestApi().create(project, repo, cpr).getBody();

        assertThat(pr).isNotNull();
        assertThat(project).isEqualTo(pr.getFromRef().getRepository().getProject().getKey());
        assertThat(repo).isEqualTo(pr.getFromRef().getRepository().getName());
        prId = pr.getId();
    }

    @Test
    public void testComment() {
        final Comments comm = api().comment(project, repo, prId, new UpdateComment(commentText)).getBody();
        assertThat(comm).isNotNull();

        assertThat(comm.getText()).isEqualTo(commentText);
        commentId = comm.getId();
    }

    @Test(dependsOnMethods = "testComment")
    public void testCreateComment() {
        final Parent parent = new Parent(commentId);
        final CreateComment createComment = new CreateComment(commentReplyText, "NORMAL", parent, null);

        final Comments comm = api().create(project, repo, prId, createComment).getBody();
        assertThat(comm).isNotNull();

        assertThat(comm.getText()).isEqualTo(commentReplyText);
        commentReplyId = comm.getId();
        commentReplyIdVersion = comm.getVersion();
    }

    @Test(dependsOnMethods = "testCreateComment")
    public void testGetComment() {
        final Comments comm = api().get(project, repo, prId, commentReplyId).getBody();
        assertThat(comm).isNotNull();

        assertThat(comm.getText().equals(commentReplyText)).isTrue();
    }

    @Test(dependsOnMethods = "testGetComment")
    public void testCreateInlineComment() {

        final ChangePage changePage = bitbucketClient.api().pullRequestApi().changes(project, repo, prId, null, null, null).getBody();
        final PullRequest pullRequest = bitbucketClient.api().pullRequestApi().get(project, repo, prId).getBody();
        assertThat(changePage).isNotNull();

        Assertions.assertThat(changePage.getValues()).hasSize(1);
        final Change change = changePage.getValues().get(0);
        this.filePath = change.getPath().toString();

        final Anchor anchor = Anchor.effective(1, this.filePath,
                Anchor.LineType.ADDED,
                Anchor.FileType.TO);
        final String randomText = TestUtilities.randomString();
        final CreateComment createComment = new CreateComment(randomText, "NORMAL", null, anchor);

        final Comments comm = api().create(project, repo, prId, createComment).getBody();
        assertThat(comm).isNotNull();

        assertThat(comm.getText()).isEqualTo(randomText);
    }

    @Test(dependsOnMethods = "testCreateInlineComment")
    public void testGetFileCommentPage() throws Exception {

        final List<Comments> allComments = new ArrayList<>();
        Integer start = null;
        while (true) {
            final CommentPage comm = api().fileComments(project, repo, prId, this.filePath, null, null, null, null,
                    start, 100).getBody();

            allComments.addAll(comm.getValues());
            start = comm.getNextPageStart();
            if (comm.isLastPage()) {
                break;
            } else {
                System.out.println("Sleeping for 1 seconds before querying for next page");
                Thread.sleep(1000);
            }
        }

        assertThat(allComments.isEmpty()).isFalse();
        boolean foundComment = false;
        for (final Comments comm : allComments) {
            if (comm.getAnchor() != null && comm.getAnchor().getPath().equalsIgnoreCase(this.filePath)) {
                foundComment = true;
                break;
            }
        }
        assertThat(foundComment).isTrue();
    }

    @Test(dependsOnMethods = "testGetFileCommentPage")
    public void testDeleteComment() {
        final ResponseEntity<Void> success = api().delete(project, repo, prId, commentReplyId, commentReplyIdVersion);
        assertThat(success).isNotNull();
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private CommentsApi api() {
        return bitbucketClient.api().commentsApi();
    }
}
