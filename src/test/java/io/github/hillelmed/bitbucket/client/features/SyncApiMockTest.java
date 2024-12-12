package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.sync.*;
import io.github.hillelmed.bitbucket.client.exception.*;
import io.github.hillelmed.bitbucket.client.options.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "unit", testName = "SyncApiMockTest")
public class SyncApiMockTest extends BaseBitbucketMockTest {

    private final String restApiPath = "/rest/sync/";
    private final String projectKey = "PRJ";
    private final String repoKey = "my-repo";
    private final String syncPath = "/projects/" + projectKey + "/repos/" + repoKey;
    private final String refsHeadsMaster = "refs/heads/master";

    public void testEnabled() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/sync-enabled.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final SyncStatus status = baseApi.syncApi().enable(projectKey, repoKey, new CreateSyncStatus(true)).getBody();
            assertThat(status.getAvailable()).isTrue();
            assertThat(status.getEnabled()).isTrue();
            Assertions.assertThat(status.getDivergedRefs()).isNotEmpty();
            Assertions.assertThat(status.getDivergedRefs().get(0).getState()).isEqualTo("DIVERGED");


            assertSent(server, postMethod, restApiPath + "latest" + syncPath);
        } finally {
            server.shutdown();
        }
    }

    public void testDisabled() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ResponseEntity<SyncStatus> status = baseApi.syncApi().enable(projectKey, repoKey, new CreateSyncStatus(true));
            assertThat(status.getStatusCode().is2xxSuccessful()).isTrue();


            assertSent(server, postMethod, restApiPath + "latest" + syncPath);
        } finally {
            server.shutdown();
        }
    }

    public void testEnabledOnError() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/errors.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            try {
                final SyncStatus status = baseApi.syncApi().enable(projectKey, repoKey, new CreateSyncStatus(true)).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, postMethod, restApiPath + "latest" + syncPath);
        } finally {
            server.shutdown();
        }
    }

    public void testStatus() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/sync-enabled.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final SyncStatus status = baseApi.syncApi().status(projectKey, repoKey, null).getBody();
            assertThat(status.getAvailable()).isTrue();
            assertThat(status.getEnabled()).isTrue();
            Assertions.assertThat(status.getDivergedRefs()).isNotEmpty();
            Assertions.assertThat(status.getDivergedRefs().get(0).getState()).isEqualTo("DIVERGED");


            assertSent(server, "GET", restApiPath + "latest" + syncPath);
        } finally {
            server.shutdown();
        }
    }

    public void testStatusAt() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/sync-enabled.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final SyncStatus status = baseApi.syncApi().status(projectKey, repoKey, "somereference").getBody();
            assertThat(status.getAvailable()).isTrue();
            assertThat(status.getEnabled()).isTrue();
            Assertions.assertThat(status.getDivergedRefs()).isNotEmpty();
            Assertions.assertThat(status.getDivergedRefs().get(0).getState()).isEqualTo("DIVERGED");


            final Map<String, ?> queryParams = Map.of("at", "somereference");
            assertSent(server, "GET", restApiPath + "latest" + syncPath, queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testStatusOnError() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/errors.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final SyncStatus status = baseApi.syncApi().status(projectKey, repoKey, null).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, "GET", restApiPath + "latest" + syncPath);
        } finally {
            server.shutdown();
        }
    }

    public void testSynchronize() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/synchronize.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final SyncOptions options = SyncOptions.merge(refsHeadsMaster);
            final SyncState ref = baseApi.syncApi().synchronize(projectKey, repoKey, options).getBody();
            assertThat(ref.getId()).isEqualTo(refsHeadsMaster);
            assertThat(ref.getState()).isEqualTo("AHEAD");


            assertSent(server, postMethod, restApiPath + "latest" + syncPath + "/synchronize");
        } finally {
            server.shutdown();
        }
    }

    public void testSynchronizeInSync() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final SyncOptions options = SyncOptions.merge(refsHeadsMaster);
            final ResponseEntity<SyncState> ref = baseApi.syncApi().synchronize(projectKey, repoKey, options);
            assertThat(ref.getStatusCode().is2xxSuccessful()).isTrue();
            assertThat(ref.getBody()).isNull(); // (204


            assertSent(server, postMethod, restApiPath + "latest" + syncPath + "/synchronize");
        } finally {
            server.shutdown();
        }
    }

    public void testSynchronizeOnError() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/errors.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {


            final SyncOptions options = SyncOptions.merge(refsHeadsMaster);

            try {
                final SyncState ref = baseApi.syncApi().synchronize(projectKey, repoKey, options).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSent(server, postMethod, restApiPath + "latest" + syncPath + "/synchronize");
        } finally {
            server.shutdown();
        }
    }
}
