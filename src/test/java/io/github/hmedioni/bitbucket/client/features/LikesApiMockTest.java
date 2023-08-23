package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.comment.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Mock tests for the {@link CommentsApi} class.
 */
@Test(groups = "unit", testName = "LikesApiMockTest")
public class LikesApiMockTest extends BaseBitbucketMockTest {

    private final String projectKey = "PRJ";
    private final String repoKey = "my-repo";
    private final String restApiPath = "/rest/comment-likes/";
    private final int pullrequestId = 101;
    private final int pullrequestCommentId = 102;

    public void getGetLikes() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-comment-likes.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final LikePage pr = baseApi.likesApi().getLikes(projectKey, repoKey, pullrequestId, pullrequestCommentId).getBody();
            assertThat(pr).isNotNull();

            Assertions.assertThat(pr.getValues()).hasSize(1);
            Assertions.assertThat(pr.getValues().get(0).getId()).isEqualTo(103);

            assertSent(server, "GET", restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/comments/102/likes");
        } finally {
            server.shutdown();
        }
    }

    public void like() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ResponseEntity<Void> pr = baseApi.likesApi().likeComment(projectKey, repoKey, pullrequestId, pullrequestCommentId);
            assertThat(pr).isNotNull();

            assertThat(pr.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server, "POST", restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/comments/102/likes");
        } finally {
            server.shutdown();
        }
    }

    public void unlike() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ResponseEntity<Void> pr = baseApi.likesApi().unlikeComment(projectKey, repoKey, pullrequestId, pullrequestCommentId);
            assertThat(pr).isNotNull();

            assertThat(pr.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server, "DELETE", restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/comments/102/likes");
        } finally {
            server.shutdown();
        }
    }
}
