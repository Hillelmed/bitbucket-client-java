package io.github.hmedioni.bitbucket.client.features;


import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.sshkey.*;
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
 * Mock tests for the {@link KeysApi} class.
 */
@Test(groups = "unit", testName = "KeysApiMockTest")
public class KeysApiMockTest extends BaseBitbucketMockTest {

    private final String projectKey = "MY-PROJECT";
    private final String repoKey = "my-repo";
    private final int keyId = 1;
    private final String getMethod = "GET";

    private final String postMethod = "POST";
    private final String deleteMethod = "DELETE";

    private final String restApiPath = "/rest/keys/";
    private final String projectsPath = "/projects/";
    private final String reposPath = "/repos/";
    private final String sshEndpoint = "/ssh";

    private final String limitKeyword = "limit";
    private final String startKeyword = "start";
    private final String keyLabel = "abc";

    public void testListKeysByRepository() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-list-by-repository.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final AccessKeyPage accessKeyPage = api.listByRepo(projectKey, repoKey, 0, 25).getBody();
            assertThat(accessKeyPage).isNotNull();

            assertThat(accessKeyPage.getSize() == 1).isTrue();
            Assertions.assertThat(accessKeyPage.getValues().get(0).getKey().getLabel().equals(keyLabel)).isTrue();
            Assertions.assertThat(accessKeyPage.getValues().get(0).getRepository().getName().equals(repoKey)).isTrue();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 25, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + sshEndpoint, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListEmptyKeysByRepository() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-list-empty.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final AccessKeyPage accessKeyPage = api.listByRepo(projectKey, repoKey, 0, 25).getBody();
            assertThat(accessKeyPage).isNotNull();
            Assertions.assertThat(accessKeyPage.getValues()).isEmpty();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 25, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + sshEndpoint, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateForRepo() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-get-for-repository.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final AccessKey accessKey = api.createForProject(projectKey, new CreateAccessKey(new CreateKey("ssh-rsa abc"),
                    AccessKey.PermissionType.REPO_READ)).getBody();
            assertThat(accessKey).isNotNull();
            assertThat(accessKey.getKey().getLabel().equals(keyLabel)).isTrue();
            assertThat(accessKey.getRepository().getName().equals(repoKey)).isTrue();

            assertSent(server, "POST", restApiPath + "latest"
                    + projectsPath + projectKey + sshEndpoint, "{\"key\": {\"text\": \"ssh-rsa abc\"},\"permission\": \"REPO_READ\"}");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateForRepoWrongKey() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-create-wrong-key.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            try {
                final AccessKey accessKey = api.createForProject(projectKey, new CreateAccessKey(new CreateKey("ssh-rsa WRONG"),
                        AccessKey.PermissionType.REPO_READ)).getBody();

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, postMethod, restApiPath + "latest"
                            + projectsPath + projectKey + sshEndpoint,
                    "{\"key\": {\"text\": \"ssh-rsa WRONG\"},\"permission\": \"REPO_READ\"}");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetForRepo() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-get-for-repository.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final AccessKey accessKey = api.getForRepo(projectKey, repoKey, keyId).getBody();
            assertThat(accessKey).isNotNull();

            assertThat(accessKey.getKey().getLabel().equals(keyLabel)).isTrue();
            assertThat(accessKey.getRepository().getName().equals(repoKey)).isTrue();

            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + sshEndpoint + "/" + keyId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetForRepoByWrongId() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-get-wrong-id.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            try {
                final AccessKey accessKey = api.getForRepo(projectKey, repoKey, keyId).getBody();

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }


            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + sshEndpoint + "/" + keyId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteFromRepo() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final ResponseEntity<Void> success = api.deleteFromRepo(projectKey, repoKey, keyId);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + sshEndpoint + "/" + keyId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteFromRepoByWrongId() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final long nonExistentKey = 2L;

            final ResponseEntity<Void> success = api.deleteFromRepo(projectKey, repoKey, nonExistentKey);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + sshEndpoint + "/" + nonExistentKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListKeysByProject() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-list-by-project.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final AccessKeyPage accessKeyPage = api.listByProject(projectKey, 0, 25).getBody();
            assertThat(accessKeyPage).isNotNull();

            assertThat(accessKeyPage.getSize() == 1).isTrue();
            Assertions.assertThat(accessKeyPage.getValues().get(0).getKey().getLabel().equalsIgnoreCase(keyLabel)).isTrue();
            Assertions.assertThat(accessKeyPage.getValues().get(0).getProject().getName().equalsIgnoreCase(projectKey)).isTrue();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 25, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + sshEndpoint, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListEmptyKeysByProject() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-list-empty.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final AccessKeyPage accessKeyPage = api.listByProject(projectKey, 0, 25).getBody();
            assertThat(accessKeyPage).isNotNull();
            Assertions.assertThat(accessKeyPage.getValues()).isEmpty();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 25, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + sshEndpoint, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateForProject() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-get-for-project.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final AccessKey accessKey = api.createForProject(projectKey, new CreateAccessKey(new CreateKey("ssh-rsa abc"),
                    AccessKey.PermissionType.PROJECT_READ)).getBody();
            assertThat(accessKey).isNotNull();
            assertThat(accessKey.getKey().getLabel().equalsIgnoreCase(keyLabel)).isTrue();
            assertThat(accessKey.getProject().getName().equalsIgnoreCase(projectKey)).isTrue();

            assertSent(server, postMethod, restApiPath + "latest"
                    + projectsPath + projectKey + sshEndpoint, "{\"key\":{\"text\": \"ssh-rsa abc\"},\"permission\": \"PROJECT_READ\"}");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateForProjectWrongKey() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-create-wrong-key.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            try {
                final AccessKey accessKey = api.createForProject(projectKey, new CreateAccessKey(new CreateKey("ssh-rsa WRONG"),
                        AccessKey.PermissionType.PROJECT_READ)).getBody();

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }
            assertSent(server, postMethod, restApiPath + "latest"
                    + projectsPath + projectKey + sshEndpoint, "{\"key\": {\"text\": \"ssh-rsa WRONG\"},\"permission\": \"PROJECT_READ\"}");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetForProject() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-get-for-project.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final AccessKey accessKey = api.getForProject(projectKey, keyId).getBody();
            assertThat(accessKey).isNotNull();

            assertThat(accessKey.getKey().getLabel().equalsIgnoreCase(keyLabel)).isTrue();
            assertThat(accessKey.getProject().getName().equalsIgnoreCase(projectKey)).isTrue();

            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + sshEndpoint + "/" + keyId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetForProjectByWrongId() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/accesskeys-get-wrong-id.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            try {
                final AccessKey accessKey = api.getForProject(projectKey, keyId).getBody();

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }


            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + sshEndpoint + "/" + keyId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteFromProject() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final ResponseEntity<Void> success = api.deleteFromProject(projectKey, keyId);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + sshEndpoint + "/" + keyId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteFromProjectByWrongId() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final KeysApi api = baseApi.keysApi();
        try {
            final long nonExistentKey = 2L;
            final ResponseEntity<Void> success = api.deleteFromProject(projectKey, nonExistentKey);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + sshEndpoint + "/" + nonExistentKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

}
