package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.build.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.options.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Mock tests for the {@link BuildStatusApiMockTest} class.
 */
@Test(groups = "unit", testName = "BuildStatusApiMockTest")
public class BuildStatusApiMockTest extends BaseBitbucketMockTest {

    private final String restBuildStatusPath = "/rest/build-status/";
    private final String commitHash = "306bcf274566f2e89f75ae6f7faf10beff38382012";
    private final String commitPath = "/commits/" + commitHash;

    public void testGetStatus() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/build-status.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final StatusPage statusPage = baseApi.buildStatusApi().status(commitHash, 0, 100).getBody();
            assertThat(statusPage).isNotNull();
            assertThat(statusPage.getSize() == 2).isTrue();
            Assertions.assertThat(statusPage.getValues().get(0).getState().equals(Status.StatusState.FAILED)).isTrue();

            final Map<String, ?> queryParams = Map.of("limit", 100, "start", 0);
            assertSent(server, "GET", restBuildStatusPath + "latest"
                    + commitPath, queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testGetStatusOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/build-status-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                baseApi.buildStatusApi().status(commitHash, 0, 100);
            } catch (BitbucketAppException e) {
                assertThat(e.errors().size() == 1).isTrue();

            }
            final Map<String, ?> queryParams = Map.of("limit", 100, "start", 0);
            assertSent(server, "GET", restBuildStatusPath + "latest"
                    + commitPath, queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testGetSummary() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/build-summary.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Summary summary = baseApi.buildStatusApi().summary(commitHash).getBody();
            assertThat(summary).isNotNull();
            assertThat(summary.getFailed() == 1).isTrue();
            assertThat(summary.getInProgress() == 2).isTrue();
            assertThat(summary.getSuccessful() == 3).isTrue();

            assertSent(server, "GET", restBuildStatusPath + "latest"
                    + "/commits/stats/306bcf274566f2e89f75ae6f7faf10beff38382012");
        } finally {
            server.shutdown();
        }
    }

    public void testAddBuildStatus() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final CreateBuildStatus cbs = new CreateBuildStatus(CreateBuildStatus.STATE.SUCCESSFUL,
                    "REPO-MASTER",
                    "REPO-MASTER-42",
                    "https://bamboo.example.com/browse/REPO-MASTER-42",
                    "Changes by John Doe");
            final ResponseEntity<Void> success = baseApi.buildStatusApi().add(commitHash, cbs);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().value()).isEqualTo(204);

            assertSent(server, "POST", restBuildStatusPath + "latest"
                    + commitPath);
        } finally {
            server.shutdown();
        }
    }

    public void testAddBuildStatusOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/errors.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final CreateBuildStatus cbs = new CreateBuildStatus(CreateBuildStatus.STATE.SUCCESSFUL,
                    "REPO-MASTER",
                    "REPO-MASTER-42",
                    "https://bamboo.example.com/browse/REPO-MASTER-42",
                    "Changes by John Doe");
            try {
                baseApi.buildStatusApi().add(commitHash, cbs);
            } catch (BitbucketAppException e) {
                assertThat(e.errors().size() == 1).isTrue();
            }

            assertSent(server, "POST", restBuildStatusPath + "latest"
                    + commitPath);
        } finally {
            server.shutdown();
        }
    }
}
