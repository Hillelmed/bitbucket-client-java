package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.postwebhooks.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import io.github.hmedioni.bitbucket.client.options.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Mock tests for the {@link PostWebHookApi} class.
 */
@Test(groups = "unit", testName = "PostWebHookApiMockTest")
public class PostWebHookApiMockTest extends BaseBitbucketMockTest {

    private final String projectKey = "PRJ";
    private final String repoKey = "my-repo";
    private final String postMethod = "POST";
    private final String getMethod = "GET";
    private final String deleteMethod = "DELETE";
    private final String putMethod = "PUT";

    private final String reposPath = "/repos/";
    private final String restApiPath = "/rest";
    private final String projectsPath = "/projects/";
    private final String webHooksEndpoint = "/webhook/";
    private final String configurations = "configurations";
    private final String postWebHookId = "1107";

    private final String postWebHookListJsonFile = "/mocks/postwebhooks-list.json";
    private final String postWebHookUpdateJsonFile = "/mocks/postwebhook-update.json";
    private final String postWebHookCreateJsonFile = "/mocks/postwebhook-create.json";
    private final String postWebHookErrorJsonFile = "/mocks/postwebhook-errors.json";
    private final String releaseBranch = "release/1.0";
    private final String userId = "userid";
    private final String newTitle = "new updated title";
    private final String newUrl = "new url";


    private final String postWebhookApiPath = restApiPath + webHooksEndpoint + "latest"
            + projectsPath + projectKey + reposPath + repoKey + webHooksEndpoint + configurations;

    public void testGetPostWebHooks() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(postWebHookListJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PostWebHookApi api = baseApi.postWebHookApi();
        try {
            final List<PostWebHook> postWebhookList = api.list(projectKey, repoKey).getBody();
            assertFalse(postWebhookList.get(0).isBranchCreated());
            assertFalse(postWebhookList.get(0).isBranchDeleted());
            assertTrue(postWebhookList.get(0).isEnabled());
            assertFalse(postWebhookList.get(0).isPrCommented());
            assertFalse(postWebhookList.get(0).isPrCreated());
            assertFalse(postWebhookList.get(0).isPrDeclined());
            assertFalse(postWebhookList.get(0).isPrMerged());
            assertFalse(postWebhookList.get(0).isPrReopened());
            assertFalse(postWebhookList.get(0).isPrRescoped());
            assertFalse(postWebhookList.get(0).isPrUpdated());
            assertFalse(postWebhookList.get(0).isRepoMirrorSynced());
            assertFalse(postWebhookList.get(0).isRepoPush());
            assertTrue(postWebhookList.get(0).isTagCreated());
            assertEquals(postWebhookList.get(0).getTitle(), "test");
            assertEquals(postWebhookList.get(0).getUrl(), "test");

            assertTrue(postWebhookList.get(1).isBranchCreated());
            assertTrue(postWebhookList.get(1).isBranchDeleted());
            assertFalse(postWebhookList.get(1).isEnabled());
            assertFalse(postWebhookList.get(1).isPrCommented());
            assertTrue(postWebhookList.get(1).isPrCreated());
            assertTrue(postWebhookList.get(1).isPrDeclined());
            assertTrue(postWebhookList.get(1).isPrMerged());
            assertTrue(postWebhookList.get(1).isPrReopened());
            assertTrue(postWebhookList.get(1).isPrRescoped());
            assertTrue(postWebhookList.get(1).isPrUpdated());
            assertFalse(postWebhookList.get(1).isRepoMirrorSynced());
            assertTrue(postWebhookList.get(1).isRepoPush());
            assertTrue(postWebhookList.get(1).isTagCreated());
            assertEquals(postWebhookList.get(1).getTitle(), "http://jenkins.example.com");
            assertEquals(postWebhookList.get(1).getUrl(), "http://jenkins.example.com/bitbucket-scmsource-hook/notify");

            assertThat(postWebhookList).isNotNull();
            assertSent(server, getMethod, postWebhookApiPath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testUpdatePostWebhook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(postWebHookUpdateJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PostWebHookApi api = baseApi.postWebHookApi();
        try {
            final CreatePostWebHook createPostWebHook = new CreatePostWebHook(
                    true, true, releaseBranch, userId,
                    true, true, true, true, true, true,
                    true, true, true, true, true, newTitle, newUrl);
            final PostWebHook postWebHook = api.update(projectKey, repoKey, postWebHookId, createPostWebHook).getBody();
            assertThat(postWebHook).isNotNull();
            assertTrue(postWebHook.isBranchCreated());
            assertTrue(postWebHook.isBranchDeleted());
            assertTrue(postWebHook.isEnabled());
            assertTrue(postWebHook.isPrCommented());
            assertTrue(postWebHook.isPrCreated());
            assertTrue(postWebHook.isPrDeclined());
            assertTrue(postWebHook.isPrMerged());
            assertTrue(postWebHook.isPrReopened());
            assertTrue(postWebHook.isPrRescoped());
            assertTrue(postWebHook.isPrUpdated());
            assertTrue(postWebHook.isRepoMirrorSynced());
            assertTrue(postWebHook.isRepoPush());
            assertTrue(postWebHook.isTagCreated());
            assertEquals(postWebHook.getTitle(), newTitle);
            assertEquals(postWebHook.getUrl(), newUrl);
            assertEquals(postWebHook.getBranchesToIgnore(), releaseBranch);

            assertSent(server, putMethod, postWebhookApiPath + "/" + postWebHookId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testUpdatePostWebhookError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(postWebHookErrorJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PostWebHookApi api = baseApi.postWebHookApi();
        try {
            final CreatePostWebHook createPostWebHook = new CreatePostWebHook(
                    true, true, releaseBranch, userId,
                    true, true, true, true, true, true,
                    true, true, true, true, true, newTitle, newUrl);
            try {
                final PostWebHook postWebHook = api.update(projectKey, repoKey, postWebHookId, createPostWebHook).getBody();

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }

            assertSent(server, putMethod, postWebhookApiPath + "/" + postWebHookId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreatePostWebhook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(postWebHookCreateJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PostWebHookApi api = baseApi.postWebHookApi();
        try {
            final CreatePostWebHook createPostWebHook = new CreatePostWebHook(
                    true, true, releaseBranch, userId,
                    true, true, true, true, true, true,
                    true, true, true, true, true, newTitle, newUrl);
            final PostWebHook postWebHook = api.create(projectKey, repoKey, createPostWebHook).getBody();
            assertThat(postWebHook).isNotNull();
            assertTrue(postWebHook.isBranchCreated());
            assertTrue(postWebHook.isBranchDeleted());
            assertTrue(postWebHook.isEnabled());
            assertTrue(postWebHook.isPrCommented());
            assertTrue(postWebHook.isPrCreated());
            assertTrue(postWebHook.isPrDeclined());
            assertTrue(postWebHook.isPrMerged());
            assertTrue(postWebHook.isPrReopened());
            assertTrue(postWebHook.isPrRescoped());
            assertTrue(postWebHook.isPrUpdated());
            assertTrue(postWebHook.isRepoMirrorSynced());
            assertTrue(postWebHook.isRepoPush());
            assertTrue(postWebHook.isTagCreated());
            assertEquals(postWebHook.getTitle(), newTitle);
            assertEquals(postWebHook.getUrl(), newUrl);
            assertEquals(postWebHook.getBranchesToIgnore(), releaseBranch);

            assertSent(server, postMethod, postWebhookApiPath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreatePostWebhookErrors() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(postWebHookErrorJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PostWebHookApi api = baseApi.postWebHookApi();
        try {
            final CreatePostWebHook createPostWebHook = new CreatePostWebHook(
                    true, true, releaseBranch, userId,
                    true, true, true, true, true, true,
                    true, true, true, true, true, newTitle, newUrl);
            try {
                final PostWebHook postWebHook = api.create(projectKey, repoKey, createPostWebHook).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, postMethod, postWebhookApiPath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePostWebhook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PostWebHookApi api = baseApi.postWebHookApi();
        try {
            final ResponseEntity<Void> ref = api.delete(projectKey, repoKey, postWebHookId);
            assertThat(ref).isNotNull();
            assertSent(server, deleteMethod, postWebhookApiPath + "/" + postWebHookId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePostWebhookErrors() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PostWebHookApi api = baseApi.postWebHookApi();
        try {
            try {
                final ResponseEntity<Void> ref = api.delete(projectKey, repoKey, postWebHookId);
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, deleteMethod, postWebhookApiPath + "/" + postWebHookId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }
}
