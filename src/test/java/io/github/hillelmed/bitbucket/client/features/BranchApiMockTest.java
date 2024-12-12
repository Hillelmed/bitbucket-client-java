package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.branch.*;
import io.github.hillelmed.bitbucket.client.domain.common.*;
import io.github.hillelmed.bitbucket.client.domain.sshkey.*;
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
 * Mock tests for the {@link BranchApi} class.
 */
@Test(groups = "unit", testName = "BranchApiMockTest")
public class BranchApiMockTest extends BaseBitbucketMockTest {

    private final String projectKey = "PRJ";
    private final String repoKey = "myrepo";
    private final String testKeyword = "test";
    private final String localRestPath = "/rest/branch-utils/";
    private final String localBranchesPath = "/branches";
    private final String localInfoPath = localBranchesPath + "/info";
    private final String localProjectsPath = "/projects/";
    private final String branchPermissionsPath = "/rest/branch-permissions/2.0";
    private final String branchModelPath = "/branchmodel/configuration";
    private final String localReposPath = "/repos/";
    private final String localGetMethod = "GET";
    private final String localDeleteMethod = "DELETE";
    private final String localLimit = "limit";
    private final String commitId = "123456789HelloWorld";

    public void testCreateBranch() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final String branchName = "dev-branch";
            final String commitHash = "8d351a10fb428c0c1239530256e21cf24f136e73";

            final CreateBranch createBranch = new CreateBranch(branchName, commitHash, null);
            final Branch branch = baseApi.branchApi().create(projectKey, repoKey, createBranch).getBody();
            assertThat(branch).isNotNull();
            assert branch.getId() != null;
            assertThat(branch.getId().endsWith(branchName)).isTrue();
            assertThat(commitHash.equalsIgnoreCase(branch.getLatestChangeset())).isTrue();
            assertSent(server, "POST", localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + localBranchesPath);
        } finally {
            server.shutdown();
        }
    }

    public void testListBranches() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-list.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final BranchPage branch = baseApi.branchApi().list(projectKey, repoKey, null, null, null, null, null, 1).getBody();
            assertThat(branch).isNotNull();
            assertThat(!branch.getValues().isEmpty()).isTrue();
            assertThat("hello-world").isEqualTo(branch.getValues().get(0).getDisplayId());
            Assertions.assertThat(branch.getValues().get(0).getMetadata()).isNotNull();

            final String jiraIssuesKey = "com.atlassian.bitbucket.server.bitbucket-jira:branch-list-jira-issues";
            final String commitInfoKey = "com.atlassian.bitbucket.server.bitbucket-branch:latest-commit-metadata";
            final String buildStatusKey = "com.atlassian.bitbucket.server.bitbucket-build:build-status-metadata";
            Assertions.assertThat(branch.getValues().get(0).getMetadata().containsKey(jiraIssuesKey)).isNotNull();
            Assertions.assertThat(branch.getValues().get(0).getMetadata().containsKey(commitInfoKey)).isNotNull();
            Assertions.assertThat(branch.getValues().get(0).getMetadata().containsKey(buildStatusKey)).isNotNull();

            final Map<String, Object> buildStatusMetadata = (Map<String, Object>) branch.getValues().get(0).getMetadata().get(buildStatusKey);
            final int success = (int) buildStatusMetadata.get("successful");
            assertThat(success).isEqualTo(1);

            final Map<String, ?> queryParams = Map.of(localLimit, 1);
            assertSent(server, localGetMethod, restBasePath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + localBranchesPath, queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testListBranchesNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-list-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final BranchPage branch = baseApi.branchApi().list(projectKey, repoKey, null, null, null, null, null, 1).getBody();
                assertThat(branch).isNotNull();
            } catch (BitbucketAppException bitbucketAppException) {
                assertThat(bitbucketAppException.errors().size() > 0).isTrue();
                final List<Veto> vetoes = bitbucketAppException.errors().get(0).getVetoes();
                assertThat(vetoes.size() > 0).isTrue();
                assertThat(vetoes.get(0).getSummaryMessage()).isEqualTo("some short message");
                assertThat(vetoes.get(0).getDetailedMessage()).isEqualTo("some detailed message");
            }
            final Map<String, ?> queryParams = Map.of("limit", 1);
            assertSent(server, "GET", "/rest/api/" + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + "/branches", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testGetBranchModel() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-model.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final BranchModel branchModel = baseApi.branchApi().model(projectKey, repoKey).getBody();
            assertThat(branchModel).isNotNull();
            assertThat(!branchModel.getTypes().isEmpty()).isTrue();
            assertSent(server, localGetMethod, localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + "/branchmodel");
        } finally {
            server.shutdown();
        }
    }

    public void testGetBranchModelOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-list-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            try {
                final BranchModel branchModel = baseApi.branchApi().model(projectKey, repoKey).getBody();
                assertThat(branchModel).isNotNull();
            } catch (BitbucketAppException b) {
                Assertions.assertThat(b.errors()).isNotEmpty();
            }
            assertSent(server, localGetMethod, localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + "/branchmodel");
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteBranch() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ResponseEntity<Void> success = baseApi.branchApi().delete(projectKey, repoKey, new DeleteBranch("refs/heads/some-branch-name"));
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().value()).isEqualTo(204);
            assertSent(server, localDeleteMethod, localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + "/branches");
        } finally {
            server.shutdown();
        }
    }

    public void testGetDefaultBranch() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-default.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Branch branch = baseApi.branchApi().getDefault(projectKey, repoKey).getBody();
            assertThat(branch).isNotNull();
            assertThat(branch.getId()).isNotNull();
            assertSent(server, localGetMethod, "/rest/api/" + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + localBranchesPath + "/default");
        } finally {
            server.shutdown();
        }
    }

    public void testGetDefaultBranchEmpty() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Branch branch = baseApi.branchApi().getDefault(projectKey, repoKey).getBody();
            assertThat(branch).isNull();
            assertSent(server, localGetMethod, restBasePath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + localBranchesPath + "/default");
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateDefaultBranch() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ResponseEntity<Void> success = baseApi.branchApi().updateDefault(projectKey, repoKey, new UpdateDefaultBranch("refs/heads/my-new-default-branch"));
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().value()).isEqualTo(204);
            assertSent(server, "PUT", "/rest/api/" + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + "/branches/default");
        } finally {
            server.shutdown();
        }
    }

    public void testListBranchePermissions() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-permission-list.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final BranchRestrictionPage branch = baseApi.branchApi().listBranchRestriction(projectKey, repoKey, null, 1).getBody();
            assertThat(branch).isNotNull();
            assertThat(!branch.getValues().isEmpty()).isTrue();
            assertThat(839L == branch.getValues().get(0).getId()).isTrue();
            assert branch.getValues().get(0).getAccessKeys() != null;
            assertThat(2 == branch.getValues().get(0).getAccessKeys().size()).isTrue();

            final Map<String, ?> queryParams = Map.of(localLimit, 1);
            assertSent(server, localGetMethod, branchPermissionsPath
                    + localProjectsPath + projectKey + localReposPath + repoKey + "/restrictions", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testListBranchesPermissionsNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-permission-list-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final BranchRestrictionPage branch = baseApi.branchApi().listBranchRestriction(projectKey, repoKey, null, 1).getBody();
                assertThat(branch).isNotNull();
            } catch (BitbucketAppException e) {
                assertThat(!e.errors().isEmpty()).isTrue();
            }

            final Map<String, ?> queryParams = Map.of(localLimit, 1);
            assertSent(server, localGetMethod, branchPermissionsPath
                    + localProjectsPath + projectKey + localReposPath + repoKey + "/restrictions", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateBranchesPermissions() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final List<String> groupPermission = new ArrayList<>();
            groupPermission.add("Test12354");
            final List<AccessKey> listAccessKey = new ArrayList<>();
            Key key = new Key();
            key.setId(123L);
            AccessKey accessKey = new AccessKey();
            accessKey.setKey(key);
            listAccessKey.add(accessKey);
            final List<BranchRestriction> listBranchPermission = new ArrayList<>();
            listBranchPermission.add(new BranchRestriction(839L, BranchRestrictionEnumType.FAST_FORWARD_ONLY,
                    new Matcher(Matcher.MatcherId.RELEASE, true), new ArrayList<>(), groupPermission,
                    listAccessKey));
            final ResponseEntity<List<BranchRestriction>> success = baseApi.branchApi().createBranchRestriction(projectKey, repoKey, listBranchPermission);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().value()).isEqualTo(204);
            assertSent(server, "POST", branchPermissionsPath
                    + localProjectsPath + projectKey + localReposPath + repoKey + "/restrictions");
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteBranchesPermissions() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Long idToDelete = 839L;
            final ResponseEntity<Void> success = baseApi.branchApi().deleteBranchRestriction(projectKey, repoKey, idToDelete);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().value()).isEqualTo(204);
            assertSent(server, localDeleteMethod, branchPermissionsPath
                    + localProjectsPath + projectKey + localReposPath + repoKey + "/restrictions/" + idToDelete);
        } finally {
            server.shutdown();
        }
    }

    public void testGetBranchModelConfiguration() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-model-configuration.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final BranchModelConfiguration configuration = baseApi.branchApi().getModelConfiguration(projectKey, repoKey).getBody();
            assertThat(configuration).isNotNull();
            assertThat(!configuration.getTypes().isEmpty()).isTrue();
            assertThat(configuration.getDevelopment().getRefId().equals("refs/heads/master")).isTrue();
            assertThat(configuration.getProduction()).isNull();
            assertSent(server, localGetMethod, localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + branchModelPath);
        } finally {
            server.shutdown();
        }
    }

    public void testGetBranchModelConfigurationOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-model-configuration-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final BranchModelConfiguration configuration = baseApi.branchApi().getModelConfiguration(projectKey, repoKey).getBody();
            } catch (BitbucketAppException e) {
                assertThat(!e.errors().isEmpty()).isTrue();
            }
            assertSent(server, localGetMethod, localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + branchModelPath);
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateBranchModelConfiguration() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-model-configuration.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final BranchConfiguration branchConfiguration = new BranchConfiguration(testKeyword, false);
            final List<Type> types = List.of(new Type(Type.TypeId.BUGFIX, testKeyword, testKeyword, true));

            final CreateBranchModelConfiguration bcm = new CreateBranchModelConfiguration(branchConfiguration, null, types);

            final BranchModelConfiguration configuration = baseApi.branchApi().updateModelConfiguration(projectKey, repoKey, bcm).getBody();
            assertThat(configuration).isNotNull();
            assertThat(configuration.getTypes().size() > 0).isTrue();
            assertSent(server, "PUT", localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + branchModelPath);
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateBranchModelConfigurationOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-model-configuration-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final BranchConfiguration branchConfiguration = new BranchConfiguration(testKeyword, false);
            final List<Type> types = List.of(new Type(Type.TypeId.BUGFIX, testKeyword, testKeyword, true));

            final CreateBranchModelConfiguration bcm = new CreateBranchModelConfiguration(branchConfiguration, null, types);

            try {
                final BranchModelConfiguration configuration = baseApi.branchApi().updateModelConfiguration(projectKey, repoKey, bcm).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSent(server, "PUT", localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + branchModelPath);
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteBranchModelConfiguration() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ResponseEntity<Void> success = baseApi.branchApi().deleteModelConfiguration(projectKey, repoKey);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().value()).isEqualTo(204);
            assertSent(server, localDeleteMethod, localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + branchModelPath);
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteBranchModelConfigurationOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                baseApi.branchApi().deleteModelConfiguration(projectKey, repoKey);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSent(server, localDeleteMethod, localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + branchModelPath);
        } finally {
            server.shutdown();
        }
    }

    public void testGetBranchInfo() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-list.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final BranchPage branchPage = baseApi.branchApi().info(projectKey, repoKey, commitId).getBody();
            assertThat(branchPage).isNotNull();
            Assertions.assertThat(branchPage.getSize()).isPositive();
            assertSent(server, localGetMethod, localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + localInfoPath + "/" + commitId);
        } finally {
            server.shutdown();
        }
    }

    public void testGetBranchInfoOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/branch-list-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                baseApi.branchApi().info(projectKey, repoKey, commitId).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSent(server, localGetMethod, localRestPath + "latest"
                    + localProjectsPath + projectKey + localReposPath + repoKey + localInfoPath + "/" + commitId);
        } finally {
            server.shutdown();
        }
    }
}
