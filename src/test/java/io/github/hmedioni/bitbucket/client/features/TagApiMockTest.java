package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.tags.*;
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
 * Mock tests for the {@link TagApi} class.
 */
@Test(groups = "unit", testName = "TagApiMockTest")
public class TagApiMockTest extends BaseBitbucketMockTest {

    private final String getMethod = "GET";
    private final String deleteMethod = "DELETE";
    private final String restApiPath = "/rest/api/";
    private final String restGitPath = "/rest/git/";
    private final String reposPath = "/repos/";
    private final String tagsPath = "/tags/";
    private final String projectsPath = "/projects/";
    private final String limitKeyword = "limit";
    private final String startKeyword = "start";

    private final String projectKey = "PRJ";
    private final String repoKey = "myrepo";

    public void testCreateTag() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/tag.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final TagApi api = baseApi.tagApi();
        try {
            final String tagName = "release-2.0.0";
            final String commitHash = "8d351a10fb428c0c1239530256e21cf24f136e73";

            final CreateTag createTag = new CreateTag(tagName, commitHash, null);
            final Tag tag = api.create(projectKey, repoKey, createTag).getBody();
            assertThat(tag).isNotNull();
            assertThat(tag.getId().endsWith(tagName)).isTrue();
            assertThat(commitHash.equalsIgnoreCase(tag.getLatestCommit())).isTrue();
            assertSent(server, "POST", restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + "/tags");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetTag() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/tag.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final TagApi api = baseApi.tagApi();
        try {
            final String tagName = "release-2.0.0";

            final Tag tag = api.get(projectKey, repoKey, tagName).getBody();
            assertThat(tag).isNotNull();
            assertThat(tag.getId().endsWith(tagName)).isTrue();

            final String commitHash = "8d351a10fb428c0c1239530256e21cf24f136e73";
            assertThat(commitHash.equalsIgnoreCase(tag.getLatestCommit())).isTrue();
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + tagsPath + tagName);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetTagNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final TagApi api = baseApi.tagApi();
        try {
            final String tagName = "non-existent-tag";
            try {
                final Tag tag = api.get(projectKey, repoKey, tagName).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + tagsPath + tagName);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListTags() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/tag-page.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final TagApi api = baseApi.tagApi();
        try {
            final TagPage tagPage = api.list(projectKey, repoKey, null, null, 0, 10).getBody();
            assertThat(tagPage).isNotNull();

            Assertions.assertThat(tagPage.getValues()).isNotEmpty();
            Assertions.assertThat(tagPage.getLimit()).isEqualTo(10);

            Assertions.assertThat(tagPage.getValues().get(0).getType()).isEqualTo("TAG");
            final Map<String, ?> queryParams = Map.of(limitKeyword, 10, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + "/tags", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListTagsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-page-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final TagApi api = baseApi.tagApi();
        try {
            try {
                final TagPage tagPage = api.list(projectKey, repoKey, null, null, 0, 10).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(limitKeyword, 10, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + "/tags", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteTag() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final TagApi api = baseApi.tagApi();
        try {
            final String tagName = "release-2.0.0";

            try {
                final ResponseEntity<Void> tag = api.delete(projectKey, repoKey, tagName);
            } catch (BitbucketAppException e) {
                assertThat(e.errors().isEmpty()).isTrue();
            }

            assertSent(server, deleteMethod, restGitPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + tagsPath + tagName);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteTagNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final TagApi api = baseApi.tagApi();
        try {
            final String tagName = "non-existent-tag";
            try {
                final ResponseEntity<Void> tag = api.delete(projectKey, repoKey, tagName);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSent(server, deleteMethod, restGitPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + tagsPath + tagName);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }
}
