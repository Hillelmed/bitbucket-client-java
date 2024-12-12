package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.project.*;
import io.github.hillelmed.bitbucket.client.exception.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import io.github.hillelmed.bitbucket.client.options.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Mock tests for the {@link ProjectApi} class.
 */
@Test(groups = "unit", testName = "ProjectApiMockTest")
public class ProjectApiMockTest extends BaseBitbucketMockTest {

    private final String localMethod = "GET";
    private final String localPath = "/projects";

    private final String projectKey = "PRJ";
    private final String getMethod = "GET";
    private final String deleteMethod = "DELETE";
    private final String putMethod = "PUT";

    private final String restApiPath = "/rest/api/";
    private final String projectsPath = "/projects/";
    private final String permissionsPath = "/permissions/";
    private final String usersPath = permissionsPath + "users";
    private final String groupsPath = permissionsPath + "groups";

    private final String limitKeyword = "limit";
    private final String startKeyword = "start";
    private final String nameKeyword = "name";
    private final String permissionKeyword = "permission";
    private final String oneTwoThreeKeyword = "123";
    private final String testOneTwoThreeKeyword = "test" + oneTwoThreeKeyword;

    public void testCreateProject() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/project.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(201));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final String projectKey = "HELLO";
            final CreateProject createProject = new CreateProject(projectKey, null, null, null);
            final Project project = baseApi.projectApi().create(createProject).getBody();

            assertThat(project).isNotNull();

            assertThat(project.getKey()).isEqualToIgnoringCase(projectKey);
            assertThat(project.getName()).isEqualToIgnoringCase(projectKey);
            Assertions.assertThat(project.links()).isNotNull();
            assertSent(server, "POST", restBasePath + "latest" + localPath);
        } finally {
            server.shutdown();
        }
    }

    public void testCreateProjectWithIllegalName() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/project-create-fail.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final String projectKey = "9999";
            final CreateProject createProject = new CreateProject(projectKey, null, null, null);
            try {
                final Project project = baseApi.projectApi().create(createProject).getBody();

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }

            assertSent(server, "POST", restBasePath + "latest" + localPath);
        } finally {
            server.shutdown();
        }
    }

    public void testGetProject() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/project.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final String projectKey = "HELLO";
            final Project project = baseApi.projectApi().get(projectKey).getBody();

            assertThat(project).isNotNull();

            assertThat(project.getKey()).isEqualToIgnoringCase(projectKey);
            Assertions.assertThat(project.links()).isNotNull();
            assertSent(server, localMethod, restBasePath + "latest" + localPath + "/" + projectKey);
        } finally {
            server.shutdown();
        }
    }

    public void testGetProjectNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/project-not-exist.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final String projectKey = "HelloWorld";
            try {
                final Project project = baseApi.projectApi().get(projectKey).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }

            assertSent(server, localMethod, restBasePath + "latest" + localPath + "/" + projectKey);
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteProject() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final String projectKey = "HELLO";
            final ResponseEntity<Void> success = baseApi.projectApi().delete(projectKey);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
            assertSent(server, "DELETE", "/rest/api/" + "latest" + "/projects/" + projectKey);
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteProjectNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/project-not-exist.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final String projectKey = "NOTEXIST";
            try {
                final ResponseEntity<Void> success = baseApi.projectApi().delete(projectKey);
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, "DELETE", "/rest/api/" + "latest" + "/projects/" + projectKey);
        } finally {
            server.shutdown();
        }
    }

    public void testGetProjectList() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/project-page-full.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ProjectPage projectPage = baseApi.projectApi().list(null, null, null, null).getBody();

            assertThat(projectPage).isNotNull();


            Assertions.assertThat(projectPage.getSize()).isLessThanOrEqualTo(projectPage.getLimit());
            Assertions.assertThat(projectPage.getStart()).isEqualTo(0);
            Assertions.assertThat(projectPage.isLastPage()).isTrue();

            Assertions.assertThat(projectPage.getValues()).hasSize(projectPage.getSize());
            Assertions.assertThat(projectPage.getValues()).hasOnlyElementsOfType(Project.class);
            assertSent(server, localMethod, restBasePath + "latest" + localPath);
        } finally {
            server.shutdown();
        }
    }

    public void testGetProjectListWithLimit() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/project-page-truncated.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final int start = 0;
            final int limit = 2;
            final ProjectPage projectPage = baseApi.projectApi().list(null, null, start, limit).getBody();

            assertThat(projectPage).isNotNull();


            final int size = projectPage.getSize();

            assertThat(size).isEqualTo(limit);
            Assertions.assertThat(projectPage.getStart()).isEqualTo(start);
            Assertions.assertThat(projectPage.getLimit()).isEqualTo(limit);
            Assertions.assertThat(projectPage.isLastPage()).isFalse();
            Assertions.assertThat(projectPage.getNextPageStart()).isEqualTo(size);

            Assertions.assertThat(projectPage.getValues()).hasSize(size);
            Assertions.assertThat(projectPage.getValues()).hasOnlyElementsOfType(Project.class);

            final Map<String, ?> queryParams = Map.of("start", start, "limit", limit);
            assertSent(server, localMethod, restBasePath + "latest" + localPath, queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testGetProjectListNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/project-not-exist.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            try {
                final ProjectPage projectPage = baseApi.projectApi().list(null, null, null, null).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }

            assertSent(server, localMethod, restBasePath + "latest" + localPath);
        } finally {
            server.shutdown();
        }
    }

    public void testCreatePermissionByUser() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {

            final ResponseEntity<Void> success = api.createPermissionsByUser(projectKey, testOneTwoThreeKeyword, oneTwoThreeKeyword);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
            final Map<String, ?> queryParams = Map.of(nameKeyword, oneTwoThreeKeyword, permissionKeyword, testOneTwoThreeKeyword);
            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + usersPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListPermissionByUser() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/project-permission-users.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {

            final ProjectPermissionsPage projectPermissionsPage = api.listPermissionsByUser(projectKey, 0, 100);
            assertThat(projectPermissionsPage).isNotNull();
            assertThat(projectPermissionsPage.getSize() == 1).isTrue();
            assertThat(projectPermissionsPage.getValues().get(0).getGroup() == null).isTrue();
            Assertions.assertThat(projectPermissionsPage.getValues().get(0).getUser().getName().equals("test")).isTrue();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + usersPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreatePermissionByGroup() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {

            final ResponseEntity<Void> success = api.createPermissionsByGroup(projectKey, testOneTwoThreeKeyword, oneTwoThreeKeyword);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
            final Map<String, ?> queryParams = Map.of(nameKeyword, oneTwoThreeKeyword, permissionKeyword, testOneTwoThreeKeyword);
            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListPermissionByGroup() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/project-permission-group.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {

            final ProjectPermissionsPage projectPermissionsPage = api.listPermissionsByGroup(projectKey, 0, 100).getBody();
            assertThat(projectPermissionsPage).isNotNull();
            assertThat(projectPermissionsPage.getSize() == 1).isTrue();
            assertThat(projectPermissionsPage.getValues().get(0).getUser() == null).isTrue();
            Assertions.assertThat(projectPermissionsPage.getValues().get(0).getGroup().getName().equals("test12345")).isTrue();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreatePermissionByUserOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {

            try {
                final ResponseEntity<Void> success = api.createPermissionsByUser(projectKey, testOneTwoThreeKeyword, oneTwoThreeKeyword);
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(nameKeyword, oneTwoThreeKeyword, permissionKeyword, testOneTwoThreeKeyword);
            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + usersPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListPermissionByUserOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/project-permission-users-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {
            try {
                final ProjectPermissionsPage projectPermissionsPage = api.listPermissionsByUser(projectKey, 0, 100);
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }


            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + usersPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreatePermissionByGroupOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {
            try {
                final ResponseEntity<Void> success = api.createPermissionsByGroup(projectKey, testOneTwoThreeKeyword, oneTwoThreeKeyword);

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }

            final Map<String, ?> queryParams = Map.of(nameKeyword, oneTwoThreeKeyword, permissionKeyword, testOneTwoThreeKeyword);
            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePermissionByUser() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {

            final ResponseEntity<Void> success = api.deletePermissionsByUser(projectKey, testOneTwoThreeKeyword);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
            final Map<String, ?> queryParams = Map.of(nameKeyword, testOneTwoThreeKeyword);
            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + usersPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePermissionByGroup() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {

            final ResponseEntity<Void> success = api.deletePermissionsByGroup(projectKey, testOneTwoThreeKeyword);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
            final Map<String, ?> queryParams = Map.of(nameKeyword, testOneTwoThreeKeyword);
            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePermissionByUserOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {
            try {
                final ResponseEntity<Void> success = api.deletePermissionsByUser(projectKey, testOneTwoThreeKeyword);

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }

            final Map<String, ?> queryParams = Map.of(nameKeyword, testOneTwoThreeKeyword);
            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + usersPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePermissionByGroupOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {
            try {
                final ResponseEntity<Void> success = api.deletePermissionsByGroup(projectKey, testOneTwoThreeKeyword);

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }

            final Map<String, ?> queryParams = Map.of(nameKeyword, testOneTwoThreeKeyword);
            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListPermissionByGroupOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/project-permission-group-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final ProjectApi api = baseApi.projectApi();
        try {

            try {
                final ProjectPermissionsPage projectPermissionsPage = api.listPermissionsByGroup(projectKey, 0, 100).getBody();

            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();

            }

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

}
