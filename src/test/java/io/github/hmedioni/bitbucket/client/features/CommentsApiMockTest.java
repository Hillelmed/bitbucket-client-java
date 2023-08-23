package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.comment.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Mock tests for the {@link CommentsApi} class.
 */
@Test(groups = "unit", testName = "CommentsApiMockTest")
public class CommentsApiMockTest extends BaseBitbucketMockTest {

    private final String projectKey = "PRJ";
    private final String repoKey = "my-repo";
    private final String restApiPath = "/rest/api/";
    private final String getMethod = "GET";
    private final String hejKeyword = "hej";
    private final String measuredReplyKeyword = "A measured reply.";

    public void testComment() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/comments.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(201));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Comments pr = baseApi.commentsApi().comment(projectKey, repoKey, 101, new UpdateComment(measuredReplyKeyword)).getBody();
            assertThat(pr).isNotNull();

            assertThat(pr.getText()).isEqualTo(measuredReplyKeyword);
            assertThat(pr.links()).isNull();
            assertSent(server, "POST", restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/comments");
        } finally {
            server.shutdown();
        }
    }

    public void testCreateComment() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/comments.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(201));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Parent parent = new Parent(1);
            final Anchor anchor = Anchor.fileLineCommit(1, "path/to/file", "path/to/file", Anchor.LineType.CONTEXT, Anchor.FileType.FROM, "22", "commitHash123123");
            final CreateComment createComment = new CreateComment(measuredReplyKeyword, "NORMAL", parent, anchor);
            final Comments pr = baseApi.commentsApi().create(projectKey, repoKey, 101, createComment).getBody();
            assertThat(pr).isNotNull();

            assertThat(pr.getText()).isEqualTo(measuredReplyKeyword);
            assertThat(pr.links()).isNull();
            assertSent(server, "POST", restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/comments");
        } finally {
            server.shutdown();
        }
    }

    public void testGetComment() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/comments.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Comments pr = baseApi.commentsApi().get(projectKey, repoKey, 101, 1).getBody();
            assertThat(pr).isNotNull();

            assertThat(pr.getText()).isEqualTo(measuredReplyKeyword);
            assertThat(pr.links()).isNull();
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/comments/1");
        } finally {
            server.shutdown();
        }
    }

    public void testGetCommentOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/commit-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final Comments pr = baseApi.commentsApi().get(projectKey, repoKey, 101, 1).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/comments/1");
        } finally {
            server.shutdown();
        }
    }

    public void testGetPullRequestComments() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-comments.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final CommentPage pcr = baseApi.commentsApi().fileComments("project", "repo", 101, hejKeyword, null, null,
                    null, null, 0, 100).getBody();
            assertThat(pcr).isNotNull();

            Assertions.assertThat(pcr.getValues().size()).isEqualTo(2);

            final Comments firstComment = pcr.getValues().get(0);
            assertThat(firstComment.getAnchor().getPath()).isEqualTo(hejKeyword);
            assertThat(firstComment.getText()).isEqualTo("comment in diff");
            assertThat(firstComment.getId()).isEqualTo(4);
            assertThat(firstComment.getComments().size()).isEqualTo(1);
            assertThat(firstComment.getComments().get(0).getText()).isEqualTo("reply to comment in diff");
            assertThat(firstComment.getComments().get(0).getId()).isEqualTo(5);
            final Map<String, Object> properties = firstComment.getProperties();
            assertThat(properties).isNotNull();
            assertThat(properties.size()).isEqualTo(1);

            final Comments secondComment = pcr.getValues().get(1);
            assertThat(secondComment.getAnchor().getPath()).isEqualTo(hejKeyword);
            assertThat(secondComment.getText()).isEqualTo("another commet in diff");
            assertThat(secondComment.getId()).isEqualTo(6);
            assertThat(secondComment.getComments().isEmpty()).isTrue();

            final Map<String, ?> queryParams = Map.of("path", hejKeyword, "start", "0", "limit", "100");
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/project/repos/repo/pull-requests/101/comments", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testGetPullRequestCommentsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/commit-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final CommentPage pcr = baseApi.commentsApi().fileComments("project", "repo", 101, hejKeyword, null, null,
                        null, null, 0, 100).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            final Map<String, ?> queryParams = Map.of("path", hejKeyword, "start", "0", "limit", "100");
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/project/repos/repo/pull-requests/101/comments", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteComment() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ResponseEntity<Void> success = baseApi.commentsApi().delete(projectKey, repoKey, 101, 1, 1);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
            final Map<String, ?> queryParams = Map.of("version", 1);
            assertSent(server, "DELETE", restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/comments/1", queryParams);
        } finally {
            server.shutdown();
        }
    }
}
