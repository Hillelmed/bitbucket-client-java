package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.labels.*;
import io.github.hmedioni.bitbucket.client.domain.repository.*;
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
 * Mock tests for the {@link RepositoryApi} class.
 */
@Test(groups = "unit", testName = "RepositoryApiMockTest")
public class RepositoryApiMockTest extends BaseBitbucketMockTest {

    private final String projectKey = "PRJ";
    private final String repoKey = "my-repo";
    private final String getMethod = "GET";
    private final String postMethod = "POST";
    private final String deleteMethod = "DELETE";
    private final String putMethod = "PUT";

    private final String restApiPath = "/rest/api/";
    private final String projectsPath = "/projects/";
    private final String permissionsPath = "/permissions/";
    private final String usersPath = permissionsPath + "users";
    private final String groupsPath = permissionsPath + "groups";
    private final String labelsPath = "/labels";
    private final String settingsPath = "/settings/";
    private final String pullRequestsPath = settingsPath + "pull-requests";
    private final String reposPath = "/repos/";
    private final String reposEndpoint = "/repos";

    private final String projectKeyword = "projectname";
    private final String limitKeyword = "limit";
    private final String startKeyword = "start";
    private final String nameKeyword = "name";
    private final String permissionKeyword = "permission";
    private final String oneTwoThreeKeyword = "123";
    private final String testOneTwoThreeKeyword = "test" + oneTwoThreeKeyword;

    public void testCreateRepository() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(201));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final CreateRepository createRepository = new CreateRepository(repoKey, repoKey, null, true);
            final Repository repository = api.create(projectKey, createRepository).getBody();
            assertThat(repository).isNotNull();

            assertThat(repository.getSlug()).isEqualToIgnoringCase(repoKey);
            assertThat(repository.getName()).isEqualToIgnoringCase(repoKey);
            assertThat(repository.links()).isNotNull();
            assertSent(server, postMethod, restApiPath + "latest" + projectsPath + projectKey + reposEndpoint);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateRepositoryWithIllegalName() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-illegal-name.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {
            final CreateRepository createRepository = new CreateRepository("!_myrepo", "aa", null, true);
            try {
                final Repository repository = api.create(projectKey, createRepository).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            assertSent(server, postMethod, restApiPath + "latest" + projectsPath + projectKey + reposEndpoint);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetRepository() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final Repository repository = api.get(projectKey, repoKey).getBody();
            assertThat(repository).isNotNull();

            assertThat(repository.getSlug()).isEqualToIgnoringCase(repoKey);
            assertThat(repository.getName()).isEqualToIgnoringCase(repoKey);
            assertThat(repository.links()).isNotNull();
            assertSent(server, getMethod, restApiPath + "latest" + projectsPath + projectKey + reposPath + repoKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetRepositoryNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {
            final String nonExistentRepoKey = "notexist";
            try {
                final Repository repository = api.get(projectKey, nonExistentRepoKey).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            assertSent(server, getMethod, restApiPath
                    + "latest"
                    + projectsPath
                    + projectKey
                    + reposPath
                    + nonExistentRepoKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testForkRepository() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-fork.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(201));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final String forkName = "hello-world";
            final Repository repository = api.fork(projectKey, repoKey, new ForkRepository(forkName, new ProjectKey(projectKey))).getBody();
            assertThat(repository).isNotNull();

            assertThat(repository.getSlug()).isEqualToIgnoringCase(forkName);
            assertThat(repository.getOrigin()).isNotNull();
            assertSent(server, postMethod, restApiPath
                    + "latest"
                    + projectsPath
                    + projectKey
                    + reposEndpoint
                    + "/" + repoKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testForkRepositoryWithErrors() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/errors.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final String forkName = "hello-world";
            final String nonExistentRepo = UUID.randomUUID().toString();
            try {
                final Repository repository = api.fork(projectKey, repoKey, new ForkRepository(nonExistentRepo, new ProjectKey(forkName))).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            assertSent(server, postMethod, restApiPath
                    + "latest"
                    + projectsPath
                    + projectKey
                    + reposEndpoint
                    + "/" + repoKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteRepository() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(202));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final ResponseEntity<Void> success = api.delete(projectKey, repoKey);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server, deleteMethod, restApiPath + "latest" + projectsPath + projectKey + reposPath + repoKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteRepositoryNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final String nonExistentRepoKey = "notexist";
            try {
                final ResponseEntity<Void> success = api.delete(projectKey, nonExistentRepoKey);
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            assertSent(server, deleteMethod, restApiPath
                    + "latest"
                    + projectsPath
                    + projectKey
                    + reposPath
                    + nonExistentRepoKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetRepositoryList() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-page-full.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final RepositoryPage repositoryPage = api.list(projectKey, null, null).getBody();

            assertSent(server, getMethod, restApiPath + "latest" + projectsPath + projectKey + reposEndpoint);
            assertThat(repositoryPage).isNotNull();


            final int size = repositoryPage.getSize();
            final int limit = repositoryPage.getLimit();

            assertThat(size).isLessThanOrEqualTo(limit);
            Assertions.assertThat(repositoryPage.getStart()).isEqualTo(0);
            Assertions.assertThat(repositoryPage.isLastPage()).isTrue();
            Assertions.assertThat(repositoryPage.getValues()).hasSize(size);
            Assertions.assertThat(repositoryPage.getValues()).hasOnlyElementsOfType(Repository.class);
        } finally {
            server.shutdown();
        }
    }

    public void testGetRepositoryListWithLimit() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-page-truncated.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final int start = 0;
            final int limit = 2;
            final RepositoryPage repositoryPage = api.list(projectKey, start, limit).getBody();

            final Map<String, ?> queryParams = Map.of(startKeyword, start, limitKeyword, limit);
            assertSent(server, getMethod, restApiPath + "latest" + projectsPath + projectKey + reposEndpoint, queryParams);
            assertThat(repositoryPage).isNotNull();


            final int size = repositoryPage.getSize();

            assertThat(size).isEqualTo(limit);
            Assertions.assertThat(repositoryPage.getStart()).isEqualTo(start);
            Assertions.assertThat(repositoryPage.getLimit()).isEqualTo(limit);
            Assertions.assertThat(repositoryPage.isLastPage()).isFalse();
            Assertions.assertThat(repositoryPage.getNextPageStart()).isEqualTo(size);
            Assertions.assertThat(repositoryPage.getValues()).hasSize(size);
            Assertions.assertThat(repositoryPage.getValues()).hasOnlyElementsOfType(Repository.class);
        } finally {
            server.shutdown();
        }
    }

    public void testGetRepositoryListNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final String nonExistentProjectKey = "non-existent";

            try {
                final RepositoryPage repositoryPage = api.list(nonExistentProjectKey, null, null).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            assertSent(server, getMethod, restApiPath + "latest" + projectsPath + nonExistentProjectKey + reposEndpoint);
        } finally {
            server.shutdown();
        }
    }

    public void testListAllRepositories() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-page-full.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final RepositoryPage repositoryPage = api.listAll(null, null, null, null, null, null).getBody();

            assertSent(server, getMethod, restApiPath + "latest"
                    + reposEndpoint);
            assertThat(repositoryPage).isNotNull();


            final int size = repositoryPage.getSize();
            final int limit = repositoryPage.getLimit();

            assertThat(size).isLessThanOrEqualTo(limit);
            Assertions.assertThat(repositoryPage.getStart()).isEqualTo(0);
            Assertions.assertThat(repositoryPage.isLastPage()).isTrue();
            Assertions.assertThat(repositoryPage.getValues()).hasSize(size);
            Assertions.assertThat(repositoryPage.getValues()).hasOnlyElementsOfType(Repository.class);
        } finally {
            server.shutdown();
        }
    }

    public void testListAllRepositoriesWithLimit() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(
                new MockResponse().setBody(payloadFromResource("/mocks/repository-page-truncated.json"))
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final int start = 0;
            final int limit = 2;
            final RepositoryPage repositoryPage = api.listAll(null, null, null, null, start, limit).getBody();

            final Map<String, ?> queryParams =
                    Map.of(startKeyword, start, limitKeyword, limit);
            assertSent(server, getMethod, restApiPath + "latest"
                    + reposEndpoint, queryParams);
            assertThat(repositoryPage).isNotNull();


            final int size = repositoryPage.getSize();

            assertThat(size).isEqualTo(limit);
            Assertions.assertThat(repositoryPage.getStart()).isEqualTo(start);
            Assertions.assertThat(repositoryPage.getLimit()).isEqualTo(limit);
            Assertions.assertThat(repositoryPage.isLastPage()).isFalse();
            Assertions.assertThat(repositoryPage.getNextPageStart()).isEqualTo(size);
            Assertions.assertThat(repositoryPage.getValues()).hasSize(size);
            Assertions.assertThat(repositoryPage.getValues()).hasOnlyElementsOfType(Repository.class);
        } finally {
            server.shutdown();
        }
    }

    public void testListAllRepositoriesByProject() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-page-single.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final RepositoryPage repositoryPage = api.listAll(projectKey, null, null, null, null, null).getBody();

            final Map<String, ?> queryParams = Map.of(projectKeyword, projectKey);
            assertSent(server, getMethod, restApiPath + "latest"
                    + reposEndpoint, queryParams);
            assertThat(repositoryPage).isNotNull();


            Assertions.assertThat(repositoryPage.getSize()).isEqualTo(1);
            Assertions.assertThat(repositoryPage.getStart()).isEqualTo(0);
            Assertions.assertThat(repositoryPage.isLastPage()).isTrue();
            Assertions.assertThat(repositoryPage.getValues()).isNotEmpty();
        } finally {
            server.shutdown();
        }
    }


    public void testListAllRepositoriesByRepository() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-page-single.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final RepositoryPage repositoryPage = api.listAll(null, repoKey, null, null, null, null).getBody();

            final Map<String, ?> queryParams = Map.of(nameKeyword, repoKey);
            assertSent(server, getMethod, restApiPath + "latest"
                    + reposEndpoint, queryParams);
            assertThat(repositoryPage).isNotNull();


            Assertions.assertThat(repositoryPage.getSize()).isEqualTo(1);
            Assertions.assertThat(repositoryPage.getStart()).isEqualTo(0);
            Assertions.assertThat(repositoryPage.isLastPage()).isTrue();
            Assertions.assertThat(repositoryPage.getValues()).isNotEmpty();
        } finally {
            server.shutdown();
        }
    }

    public void testListAllRepositoriesByProjectNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-page-empty.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final RepositoryPage repositoryPage = api.listAll(projectKey, null, null, null, null, null).getBody();

            final Map<String, ?> queryParams = Map.of(projectKeyword, projectKey);
            assertSent(server, getMethod, restApiPath + "latest"
                    + reposEndpoint, queryParams);
            assertThat(repositoryPage).isNotNull();


            Assertions.assertThat(repositoryPage.getSize()).isEqualTo(0);
            Assertions.assertThat(repositoryPage.getStart()).isEqualTo(0);
            Assertions.assertThat(repositoryPage.isLastPage()).isTrue();
            Assertions.assertThat(repositoryPage.getValues()).isEmpty();
        } finally {
            server.shutdown();
        }
    }

    public void testListAllRepositoriesByRepositoryNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-page-empty.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final RepositoryPage repositoryPage = api.listAll(null, repoKey, null, null, null, null).getBody();

            final Map<String, ?> queryParams = Map.of(nameKeyword, repoKey);
            assertSent(server, getMethod, restApiPath + "latest"
                    + reposEndpoint, queryParams);
            assertThat(repositoryPage).isNotNull();


            Assertions.assertThat(repositoryPage.getSize()).isEqualTo(0);
            Assertions.assertThat(repositoryPage.getStart()).isEqualTo(0);
            Assertions.assertThat(repositoryPage.isLastPage()).isTrue();
            Assertions.assertThat(repositoryPage.getValues()).isEmpty();
        } finally {
            server.shutdown();
        }
    }

    public void testGetPullRequestSettings() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-settings.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();
            final PullRequestSettings settings = api.getPullRequestSettings(projectKey, repoKey);

            assertThat(settings).isNotNull();

            assertThat(settings.getRequiredAllApprovers()).isFalse();
            assertThat(settings.getRequiredAllTasksComplete()).isTrue();
            assertThat(settings.getUnapproveOnUpdate()).isTrue();
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath);
        } finally {
            server.shutdown();
        }
    }

    public void testCreatePermissionByUser() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final ResponseEntity<Void> success = api.createPermissionsByUser(projectKey, repoKey, testOneTwoThreeKeyword, oneTwoThreeKeyword);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            final Map<String, ?> queryParams = Map.of(nameKeyword, oneTwoThreeKeyword, permissionKeyword, testOneTwoThreeKeyword);
            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + usersPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListPermissionByUser() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-permission-users.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final PermissionsPage permissionsPage = api.listPermissionsByUser(projectKey, repoKey, 0, 100).getBody();
            assertThat(permissionsPage).isNotNull();

            assertThat(permissionsPage.getSize() == 1).isTrue();
            assertThat(permissionsPage.getValues().get(0).getGroup() == null).isTrue();
            Assertions.assertThat(permissionsPage.getValues().get(0).getUser().getName().equals("test")).isTrue();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + usersPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestSettingsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-settings-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            try {
                final PullRequestSettings settings = api.getPullRequestSettings(projectKey, repoKey);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath);
        } finally {
            server.shutdown();
        }
    }

    public void testCreatePermissionByGroup() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final ResponseEntity<Void> success = api.createPermissionsByGroup(projectKey, repoKey, testOneTwoThreeKeyword, oneTwoThreeKeyword);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            final Map<String, ?> queryParams = Map.of(nameKeyword, oneTwoThreeKeyword, permissionKeyword, testOneTwoThreeKeyword);
            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListPermissionByGroup() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-permission-group.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final PermissionsPage permissionsPage = api.listPermissionsByGroup(projectKey, repoKey, 0, 100).getBody();
            assertThat(permissionsPage).isNotNull();

            assertThat(permissionsPage.getSize() == 1).isTrue();
            assertThat(permissionsPage.getValues().get(0).getUser() == null).isTrue();
            Assertions.assertThat(permissionsPage.getValues().get(0).getGroup().getName().equals("test12345")).isTrue();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testUpdatePullRequestSettings() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-settings.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final MergeStrategy strategy = new MergeStrategy(null, null, null, MergeStrategy.MergeStrategyId.FF, null);
            final List<MergeStrategy> listStrategy = new ArrayList<>();
            listStrategy.add(strategy);
            final MergeConfig mergeConfig = new MergeConfig(strategy, listStrategy, MergeConfig.MergeConfigType.REPOSITORY, 20);
            final CreatePullRequestSettings pullRequestSettings = new CreatePullRequestSettings(mergeConfig, false, false, 0, 1, true);
            final PullRequestSettings settings = api.updatePullRequestSettings(projectKey, repoKey, pullRequestSettings).getBody();

            assertThat(settings).isNotNull();

            assertThat(settings.getRequiredAllApprovers()).isFalse();
            assertThat(settings.getRequiredAllTasksComplete()).isTrue();
            assertSent(server, postMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath);
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
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            try {
                final ResponseEntity<Void> success = api.createPermissionsByUser(projectKey, repoKey, testOneTwoThreeKeyword, oneTwoThreeKeyword);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(nameKeyword, oneTwoThreeKeyword, permissionKeyword, testOneTwoThreeKeyword);
            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + usersPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testUpdatePullRequestSettingsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-settings-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final RepositoryApi api = baseApi.repositoryApi();

            final MergeStrategy strategy = new MergeStrategy(null, null, null, MergeStrategy.MergeStrategyId.FF, null);
            final List<MergeStrategy> listStrategy = new ArrayList<>();
            listStrategy.add(strategy);
            final MergeConfig mergeConfig = new MergeConfig(strategy, listStrategy, MergeConfig.MergeConfigType.REPOSITORY, 20);
            final CreatePullRequestSettings pullRequestSettings = new CreatePullRequestSettings(mergeConfig, false, false, 0, 1, true);

            try {
                final PullRequestSettings settings = api.updatePullRequestSettings(projectKey, repoKey, pullRequestSettings).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSent(server, postMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath);
        } finally {
            server.shutdown();
        }
    }

    public void testListPermissionByUserOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-permission-users-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            try {
                final PermissionsPage permissionsPage = api.listPermissionsByUser(projectKey, repoKey, 0, 100).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + usersPath, queryParams);
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
        final RepositoryApi api = baseApi.repositoryApi();
        try {


            try {
                final ResponseEntity<Void> success = api.createPermissionsByGroup(projectKey, repoKey, testOneTwoThreeKeyword, oneTwoThreeKeyword);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(nameKeyword, oneTwoThreeKeyword, permissionKeyword, testOneTwoThreeKeyword);
            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + groupsPath, queryParams);
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
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final ResponseEntity<Void> success = api.deletePermissionsByUser(projectKey, repoKey, testOneTwoThreeKeyword);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            final Map<String, ?> queryParams = Map.of(nameKeyword, testOneTwoThreeKeyword);
            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + usersPath, queryParams);
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
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            final ResponseEntity<Void> success = api.deletePermissionsByGroup(projectKey, repoKey, testOneTwoThreeKeyword);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            final Map<String, ?> queryParams = Map.of(nameKeyword, testOneTwoThreeKeyword);
            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + groupsPath, queryParams);
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
        final RepositoryApi api = baseApi.repositoryApi();
        try {
            try {
                final ResponseEntity<Void> success = api.deletePermissionsByUser(projectKey, repoKey, testOneTwoThreeKeyword);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(nameKeyword, testOneTwoThreeKeyword);
            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + usersPath, queryParams);
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
        final RepositoryApi api = baseApi.repositoryApi();
        try {
            try {
                final ResponseEntity<Void> success = api.deletePermissionsByGroup(projectKey, repoKey, testOneTwoThreeKeyword);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(nameKeyword, testOneTwoThreeKeyword);
            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListPermissionByGroupOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-permission-group-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {

            try {
                final PermissionsPage permissionsPage = api.listPermissionsByGroup(projectKey, repoKey, 0, 100).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + groupsPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetLabels() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-labels.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final RepositoryApi api = baseApi.repositoryApi();
        try {
            final LabelsPage labelsPage = api.getLabels(projectKey, repoKey).getBody();
            assertThat(labelsPage).isNotNull();

            assertThat(labelsPage.getValues()).isNotEmpty();
            assertSent(server,
                    getMethod,
                    restBasePath + "latest" + projectsPath + projectKey + reposPath + repoKey + labelsPath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }
}
