package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.commit.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Mock tests for the {@link CommitsApi} class.
 */
@Test(groups = "unit", testName = "CommitApiMockTest")
public class CommitsApiMockTest extends BaseBitbucketMockTest {

    private final String projectKey = "PRJ";
    private final String repoKey = "myrepo";
    private final String commitHash = "abcdef0123abcdef4567abcdef8987abcdef6543";

    private final String getMethod = "GET";
    private final String restApiPath = "/rest/api/";
    private final String limitKeyword = "limit";

    public void testGetCommit() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/commit.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Commit commit = baseApi.commitsApi().get(projectKey, repoKey, commitHash, null).getBody();
            assertThat(commit).isNotNull();

            assertThat(commit.getId().equalsIgnoreCase(commitHash)).isTrue();
            assertThat(commit.getAuthorTimestamp()).isNotNull().isNotEqualTo(0);
            assertThat(commit.getAuthor()).isNotNull();
            assertThat(commit.getCommitterTimestamp()).isNotNull().isNotEqualTo(0);
            assertThat(commit.getCommitter()).isNotNull();
            assertThat(commit.getProperties()).isNotNull().isNotEqualTo(0);

            assertSent(server, getMethod, restBasePath + "latest"
                    + "/projects/" + projectKey + "/repos/" + repoKey + "/commits/" + commitHash);
        } finally {
            server.shutdown();
        }
    }

    public void testGetCommitNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/commit-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            try {
                final Commit commit = baseApi.commitsApi().get(projectKey, repoKey, commitHash, null).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e.errors().size() > 0).isTrue();

            }
            assertSent(server, getMethod, restBasePath + "latest"
                    + "/projects/" + projectKey + "/repos/" + repoKey + "/commits/" + commitHash);
        } finally {
            server.shutdown();
        }
    }

    public void testGetPullRequestChanges() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/pull-request-changes.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ChangePage changePage = baseApi.commitsApi().listChanges(projectKey, repoKey, commitHash, 12, null).getBody();
            assertThat(changePage).isNotNull();
            Assertions.assertThat(changePage.getValues()).hasSize(1);

            final Map<String, ?> queryParams = Map.of(limitKeyword, 12);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/myrepo/commits/abcdef0123abcdef4567abcdef8987abcdef6543/changes", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testGetPullRequestChangesOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/commit-error.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            try {
                final ChangePage changePage = baseApi.commitsApi().listChanges(projectKey, repoKey, commitHash, 1, 12).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(limitKeyword, 1, "start", 12);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/myrepo/commits/abcdef0123abcdef4567abcdef8987abcdef6543/changes", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testListCommits() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-commits.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final CommitPage pcr = baseApi.commitsApi().list(projectKey, repoKey, true, null, null, null, null, null, null, 1, null).getBody();
            assertThat(pcr).isNotNull();

            Assertions.assertThat(pcr.getValues()).hasSize(1);
            assertThat(pcr.getTotalCount()).isEqualTo(1);

            final Map<String, ?> queryParams = Map.of("withCounts", true, limitKeyword, 1);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/myrepo/commits", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testListCommitsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/commit-error.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final CommitPage pcr = baseApi.commitsApi().list(projectKey, repoKey, true, null, null, null, null, null, null, 1, null).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of("withCounts", true, limitKeyword, 1);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/myrepo/commits", queryParams);
        } finally {
            server.shutdown();
        }
    }
}
