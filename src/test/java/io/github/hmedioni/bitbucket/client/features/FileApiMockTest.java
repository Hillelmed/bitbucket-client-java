package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.commit.*;
import io.github.hmedioni.bitbucket.client.domain.file.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "unit", testName = "FileApiMockTest")
public class FileApiMockTest extends BaseBitbucketMockTest {

    private final String projectKey = "PRJ";
    private final String repoKey = "myrepo";
    private final String filePath = "some/random/path/MyFile.txt";
    private final String directoryPath = "some/random/path";
    private final String branch = "myBranch";
    private final String content = "file contents";
    private final String getMethod = "GET";
    private final String putMethod = "PUT";
    private final String restApiPath = "/rest/api/" + "latest";
    private final String projectRepoPath = "/projects/" + projectKey + "/repos/" + repoKey;
    private final String rawPath = projectRepoPath + "/raw/";
    private final String browsePath = restApiPath + projectRepoPath + "/browse/";
    private final String filesPath = restApiPath + projectRepoPath + "/files";
    private final String lastModifiedPath = restApiPath + projectRepoPath + "/last-modified";
    private final String errorResponseBody = payloadFromResource("/mocks/errors.json");
    private final String lastModifiedResposnseBody = payloadFromResource("/mocks/last-modified.json");

    public void testGetContent() throws Exception {
        final MockWebServer server = mockWebServer();

        final String content = "Hello, World!";
        server.enqueue(new MockResponse().setBody(content).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final FileApi api = baseApi.fileApi();
        try {

            final String rawContent = api.raw(projectKey, repoKey, filePath, null).getBody();
            assertThat(rawContent).isNotNull();
            assertSentAcceptText(server, getMethod, rawPath + filePath);

        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetContentOnNotFound() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody("<html>randomString</html>").setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final FileApi api = baseApi.fileApi();
        try {
            try {
                final String rawContent = api.raw(projectKey, repoKey, filePath, null).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSentAcceptText(server, getMethod, rawPath + filePath);

        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListLines() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/line-page.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final FileApi api = baseApi.fileApi();
        try {

            final LinePage linePage = api.listLines(projectKey, repoKey, filePath, null, null, null, null, null, null).getBody();
            assertThat(linePage).isNotNull();
            assertThat(linePage.getLines().isEmpty()).isFalse();
            assertThat(linePage.getLines().get(0).getText()).isEqualTo("BEARS");
            assertSent(server, getMethod, browsePath + filePath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListLinesWithBlame() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/line-page-with-blame.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final FileApi api = baseApi.fileApi();
        try {

            final LinePage linePage = api.listLines(projectKey, repoKey, filePath, null, null, true, null, null, null).getBody();
            assertThat(linePage).isNotNull();
            assertThat(linePage.getBlame().isEmpty()).isFalse();
            assertThat(linePage.getBlame().get(0).getDisplayCommitHash()).isEqualTo("cba97d2b0c7");
            assertThat(linePage.getBlame().get(0).getAuthor().getName()).isEqualTo("JordanPeterson");

            final Map<String, ?> queryParams = Map.of("blame", "true");
            assertSent(server, getMethod, browsePath + filePath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListLinesOnNotFound() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-page-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final FileApi api = baseApi.fileApi();
        try {
            try {
                final LinePage linePage = api.listLines(projectKey, repoKey, filePath, null, null, null, null, null, null).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e.errors().isEmpty()).isFalse();
            }
            assertSent(server, getMethod, browsePath + filePath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testUpdateContent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/commit.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final Commit commit = baseApi.fileApi().updateContent(projectKey, repoKey, filePath, branch, content, null, null, null).getBody();
            assertThat(commit).isNotNull();
            assertSent(server, putMethod, browsePath + filePath);
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateContentBadRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(errorResponseBody).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final Commit commit = baseApi.fileApi().updateContent(projectKey, repoKey, filePath, branch, content, null, null, null).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e.errors().isEmpty()).isFalse();
            }
            assertSent(server, putMethod, browsePath + filePath);
        } finally {
            server.shutdown();
        }
    }

    public void testListFiles() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/files-page.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final FileApi api = baseApi.fileApi();
        try {

            final String gitRef = "some-existing-commit-or-tag-or-branch";
            final FilesPage ref = api.listFiles(projectKey, repoKey, gitRef, null, null).getBody();
            assertThat(ref).isNotNull();
            Assertions.assertThat(ref.getValues().isEmpty()).isFalse();
            Assertions.assertThat(ref.getValues().get(0)).isEqualTo("path/to/file.txt");

            final Map<String, ?> queryParams = Map.of("at", gitRef);
            assertSent(server, getMethod, filesPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListFilesAtPath() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/files-page.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final FileApi api = baseApi.fileApi();
        try {

            final String gitRef = "some-existing-commit-or-tag-or-branch";
            final int start = 10;
            final int limit = 25;
            final FilesPage ref = api.listFiles(projectKey, repoKey, directoryPath, gitRef, start, limit).getBody();
            assertThat(ref).isNotNull();
            Assertions.assertThat(ref.getValues().isEmpty()).isFalse();
            Assertions.assertThat(ref.getValues().get(0)).isEqualTo("path/to/file.txt");

            final Map<String, ?> queryParams = Map.of("at", gitRef, "start", start, "limit", limit);
            assertSent(server, getMethod, filesPath + "/" + directoryPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListFilesOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(errorResponseBody).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final FileApi api = baseApi.fileApi();
        try {

            final String gitRef = "some-NON-existing-commit-or-tag-or-branch";
            try {
                final FilesPage ref = api.listFiles(projectKey, repoKey, gitRef, null, null).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of("at", gitRef);
            assertSent(server, getMethod, filesPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testLastModified() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(lastModifiedResposnseBody).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final LastModified summary = baseApi.fileApi().lastModified(projectKey, repoKey, branch).getBody();
            assertThat(summary).isNotNull();
            Assertions.assertThat(summary.getLatestCommit()).isNotNull();
            assert summary.getFiles() != null;
            Assertions.assertThat(summary.getFiles()).isNotEmpty();
            assertSent(server, getMethod, lastModifiedPath, Collections.singletonMap("at", branch));
        } finally {
            server.shutdown();
        }
    }

    public void testLastModifiedGivenEmptyStringPath() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(lastModifiedResposnseBody).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final LastModified summary = baseApi.fileApi().lastModified(projectKey, repoKey, "", branch).getBody();
            assertThat(summary).isNotNull();
            Assertions.assertThat(summary.getLatestCommit()).isNotNull();
            Assertions.assertThat(summary.getFiles()).isNotEmpty();
            assertSent(server, getMethod, lastModifiedPath, Collections.singletonMap("at", branch));
        } finally {
            server.shutdown();
        }
    }

    public void testLastModifiedGivenSlashPath() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(lastModifiedResposnseBody).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final LastModified summary = baseApi.fileApi().lastModified(projectKey, repoKey, "/", branch).getBody();
            assertThat(summary).isNotNull();
            Assertions.assertThat(summary.getLatestCommit()).isNotNull();
            Assertions.assertThat(summary.getFiles()).isNotEmpty();
            assertSent(server, getMethod, lastModifiedPath + "/", Collections.singletonMap("at", branch));
        } finally {
            server.shutdown();
        }
    }

    public void testLastModifiedAtPath() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(lastModifiedResposnseBody).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final LastModified summary = baseApi.fileApi().lastModified(projectKey, repoKey, directoryPath, branch).getBody();
            assertThat(summary).isNotNull();
            Assertions.assertThat(summary.getLatestCommit()).isNotNull();
            Assertions.assertThat(summary.getFiles()).isNotEmpty();
            assertSent(server, getMethod, lastModifiedPath + "/" + directoryPath, Collections.singletonMap("at", branch));
        } finally {
            server.shutdown();
        }
    }

    public void testLastModifiedBadRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(errorResponseBody).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final LastModified summary = baseApi.fileApi().lastModified(projectKey, repoKey, branch).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSent(server, getMethod, lastModifiedPath, Collections.singletonMap("at", branch));
        } finally {
            server.shutdown();
        }
    }

    public void testLastModifiedAtPathBadRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(errorResponseBody).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final LastModified summary = baseApi.fileApi().lastModified(projectKey, repoKey, directoryPath, branch).getBody();

            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSent(server, getMethod, lastModifiedPath + "/" + directoryPath, Collections.singletonMap("at", branch));
        } finally {
            server.shutdown();
        }
    }
}
