package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.activities.*;
import io.github.hillelmed.bitbucket.client.domain.comment.*;
import io.github.hillelmed.bitbucket.client.domain.commit.*;
import io.github.hillelmed.bitbucket.client.domain.common.*;
import io.github.hillelmed.bitbucket.client.domain.participants.*;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.*;
import io.github.hillelmed.bitbucket.client.exception.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import io.github.hillelmed.bitbucket.client.options.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Mock tests for the {@link PullRequestApi} class.
 */
@Test(groups = "unit", testName = "PullRequestApiMockTest")
public class PullRequestApiMockTest extends BaseBitbucketMockTest {

    private static final String BOB_EMAIL_ADDRESS = "bob@acme.ic";
    private static final String USER_TYPE = "asd";
    private static final String PARTICIPANTS_PATH = "/participants/";

    private final String projectKey = "PRJ";
    private final String repoKey = "my-repo";
    private final String restApiPath = "/rest/api/";
    private final String getMethod = "GET";
    private final String postMethod = "POST";
    private final String putMethod = "PUT";
    private final String deleteMethod = "DELETE";

    private final String limitKeyword = "limit";
    private final String versionKeyword = "version";
    private final String startKeyword = "start";
    private final String projectsPath = "/projects/";
    private final String pullRequestsPath = "/pull-requests/";
    private final String specificPullRequestPath = "/projects/PRJ/repos/my-repo/pull-requests/101";
    private final String specificPullRequestMergePath = specificPullRequestPath + "/merge";
    private final String bobKeyword = "bob";
    private final String reposPath = "/repos/";
    private final String pullRequestFile = "/mocks/pull-request.json";

    public void testCreatePullRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(pullRequestFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(201));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final ProjectKey proj1 = new ProjectKey(projectKey);
            final ProjectKey proj2 = new ProjectKey(projectKey);
            final MinimalRepository repository1 = new MinimalRepository(repoKey, null, proj1);
            final MinimalRepository repository2 = new MinimalRepository(repoKey, null, proj2);

            final String commitId = "930228bb501e07c2653771858320873d94518e33";
            final Reference fromRef = new Reference("refs/heads/feature-ABC-123", repository1, null, null, "feature-ABC-123", commitId);
            final Reference toRef = new Reference("refs/heads/master", repository2);
            final CreatePullRequest cpr = new CreatePullRequest("Talking Nerdy", "Some description", fromRef, toRef);
            final PullRequest pr = api.create(repository2.getProject().getKey(), repository2.getSlug(), cpr).getBody();

            assertThat(pr).isNotNull();

            Assertions.assertThat(pr.getFromRef().getRepository().getProject().getKey()).isEqualToIgnoringCase(projectKey);
            Assertions.assertThat(pr.getFromRef().getRepository().getSlug()).isEqualToIgnoringCase(repoKey);
            assertThat(pr.getId()).isEqualTo(101);
            Assertions.assertThat(pr.links()).isNotNull();
            Assertions.assertThat(pr.getFromRef().getLatestCommit().equals(commitId));
            assertSent(server, postMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testEditPullRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(pullRequestFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final ProjectKey proj = new ProjectKey(projectKey);
            final MinimalRepository repository = new MinimalRepository(repoKey, null, proj);

            final String commitId = "930228bb501e07c2653771858320873d94518e33";
            final EditPullRequest epr = new EditPullRequest(101, 1, "Talking Nerdy", "Edited description", null);
            final PullRequest pr = api.edit(repository.getProject().getKey(), repository.getSlug(), 101, epr).getBody();

            assertThat(pr).isNotNull();

            Assertions.assertThat(pr.getFromRef().getRepository().getProject().getKey()).isEqualToIgnoringCase(projectKey);
            Assertions.assertThat(pr.getFromRef().getRepository().getSlug()).isEqualToIgnoringCase(repoKey);
            assertThat(pr.getId()).isEqualTo(101);
            Assertions.assertThat(pr.links()).isNotNull();
            Assertions.assertThat(pr.getFromRef().getLatestCommit().equals(commitId));
            assertSent(server, putMethod, restApiPath + "latest"
                    + specificPullRequestPath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(pullRequestFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final PullRequest pr = api.get(projectKey, repoKey, 101).getBody();
            assertThat(pr).isNotNull();

            Assertions.assertThat(pr.getFromRef().getRepository().getProject().getKey()).isEqualToIgnoringCase(projectKey);
            Assertions.assertThat(pr.getFromRef().getRepository().getSlug()).isEqualToIgnoringCase(repoKey);
            assertThat(pr.getId()).isEqualTo(101);
            Assertions.assertThat(pr.links()).isNotNull();
            assertSent(server, getMethod, restApiPath + "latest"
                    + specificPullRequestPath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListPullRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-page.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final PullRequestPage pr = api.list(projectKey, repoKey, null, null, null, null, null, null, null, 10).getBody();
            assertThat(pr).isNotNull();

            Assertions.assertThat(pr.getValues()).isNotEmpty();
            final Map<String, ?> queryParams = Map.of(limitKeyword, 10);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListPullRequestNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-page-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            try {
                final PullRequestPage pr = api.list(projectKey, repoKey, null, null, null, null, null, null, null, 10).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(limitKeyword, 10);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeclinePullRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-decline.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final PullRequest pr = api.decline(projectKey, repoKey, 101, 1).getBody();
            assertThat(pr).isNotNull();

            Assertions.assertThat(pr.getFromRef().getRepository().getProject().getKey()).isEqualToIgnoringCase(projectKey);
            Assertions.assertThat(pr.getFromRef().getRepository().getSlug()).isEqualToIgnoringCase(repoKey);
            assertThat(pr.getId()).isEqualTo(101);
            assertThat(pr.getState()).isEqualToIgnoringCase("DECLINED");
            assertThat(pr.isOpen()).isFalse();
            Assertions.assertThat(pr.links()).isNotNull();

            final Map<String, ?> queryParams = Map.of(versionKeyword, 1);
            assertSent(server, postMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/decline", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testReopenPullRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(pullRequestFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final PullRequest pr = api.reopen(projectKey, repoKey, 101, 1).getBody();
            assertThat(pr).isNotNull();

            Assertions.assertThat(pr.getFromRef().getRepository().getProject().getKey()).isEqualToIgnoringCase(projectKey);
            Assertions.assertThat(pr.getFromRef().getRepository().getSlug()).isEqualToIgnoringCase(repoKey);
            assertThat(pr.getId()).isEqualTo(101);
            assertThat(pr.getState()).isEqualToIgnoringCase("OPEN");
            assertThat(pr.isOpen()).isTrue();
            Assertions.assertThat(pr.links()).isNotNull();

            final Map<String, ?> queryParams = Map.of(versionKeyword, 1);
            assertSent(server, postMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/reopen", queryParams);

        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCanMergePullRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-can-merge-succeed.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final MergeStatus ms = api.canMerge(projectKey, repoKey, 101).getBody();
            assertThat(ms).isNotNull();

            assertThat(ms.isCanMerge()).isTrue();
            Assertions.assertThat(ms.getVetoes()).isEmpty();
            assertSent(server, getMethod, restApiPath + "latest"
                    + specificPullRequestMergePath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCanMergePullRequestFail() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-can-merge-fail.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final MergeStatus ms = api.canMerge(projectKey, repoKey, 101).getBody();
            assertThat(ms).isNotNull();

            assertThat(ms.isCanMerge()).isFalse();
            Assertions.assertThat(ms.getVetoes()).hasSize(1);
            assertSent(server, getMethod, restApiPath + "latest"
                    + specificPullRequestMergePath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testMergePullRequest() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-merge.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final PullRequest pr = api.merge(projectKey, repoKey, 101, 1).getBody();
            assertThat(pr).isNotNull();

            Assertions.assertThat(pr.getFromRef().getRepository().getProject().getKey()).isEqualToIgnoringCase(projectKey);
            Assertions.assertThat(pr.getFromRef().getRepository().getSlug()).isEqualToIgnoringCase(repoKey);
            assertThat(pr.getId()).isEqualTo(101);
            assertThat(pr.getState()).isEqualToIgnoringCase("MERGED");
            assertThat(pr.isOpen()).isFalse();
            Assertions.assertThat(pr.links()).isNotNull();

            final Map<String, ?> queryParams = Map.of(versionKeyword, 1);
            assertSent(server, postMethod, restApiPath + "latest"
                    + specificPullRequestMergePath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testMergePullRequestNeedsRetry() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/merge-failed-retry.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(409));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final AtomicInteger retries = new AtomicInteger(5);

            PullRequest pr = null;
            try {
                pr = api.merge(projectKey, repoKey, 101, 1).getBody();
            } catch (BitbucketAppException e) {
                BitbucketAppException bitbucketAppException = e;
                while (retries.get() > 0 && !bitbucketAppException.errors().isEmpty() && bitbucketAppException.errors().get(0).getMessage().contains("Please retry the merge")) {

                    System.out.println("Bitbucket is under load. Waiting for some time period and then retrying");
                    Thread.sleep(500);
                    retries.decrementAndGet();

                    if (retries.get() == 0) {
                        server.enqueue(new MockResponse()
                                .setBody(payloadFromResource("/mocks/pull-request-merge.json"))
                                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(200));
                    } else {
                        server.enqueue(new MockResponse()
                                .setBody(payloadFromResource("/mocks/merge-failed-retry.json"))
                                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(409));
                    }
                    try {
                        pr = api.merge(projectKey, repoKey, 101, 1).getBody();
                    } catch (BitbucketAppException e1) {
                        bitbucketAppException = e1;
                    }
                }
            }

            assertThat(pr).isNotNull();

            Assertions.assertThat(pr.getFromRef().getRepository().getProject().getKey()).isEqualToIgnoringCase(projectKey);
            Assertions.assertThat(pr.getFromRef().getRepository().getSlug()).isEqualToIgnoringCase(repoKey);
            assertThat(pr.getId()).isEqualTo(101);
            assertThat(pr.getState()).isEqualToIgnoringCase("MERGED");
            assertThat(pr.isOpen()).isFalse();
            Assertions.assertThat(pr.links()).isNotNull();

            final Map<String, ?> queryParams = Map.of(versionKeyword, 1);
            assertSent(server, postMethod, restApiPath + "latest"
                    + specificPullRequestMergePath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestNonExistent() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            try {
                final PullRequest pr = api.get(projectKey, repoKey, 101).getBody();
            } catch (BitbucketAppException e) {

                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
                Assertions.assertThat(e.errors().get(0).getExceptionName()).endsWith("NoSuchPullRequestException");
            }
            assertSent(server, getMethod, restApiPath + "latest"
                    + specificPullRequestPath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestChanges() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-changes.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final ChangePage pcr = api.changes(projectKey, repoKey, 101, true, 12, null).getBody();
            assertThat(pcr).isNotNull();

            Assertions.assertThat(pcr.getValues()).hasSize(1);

            final Map<String, ?> queryParams = Map.of("withComments", true, limitKeyword, 12);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/changes", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestChangesOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/commit-error.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            try {
                final ChangePage pcr = api.changes(projectKey, repoKey, 101, true, 12, null).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();

                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of("withComments", true, limitKeyword, 12);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/changes", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestCommits() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-commits.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final CommitPage pcr = api.commits(projectKey, repoKey, 101, true, 1, null).getBody();
            assertThat(pcr).isNotNull();

            Assertions.assertThat(pcr.getValues()).hasSize(1);
            assertThat(pcr.getTotalCount()).isEqualTo(1);

            final Map<String, ?> queryParams = Map.of("withCounts", true, limitKeyword, 1);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/commits", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestCommitsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/commit-error.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            try {
                final CommitPage pcr = api.commits(projectKey, repoKey, 101, true, 1, null).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            final Map<String, ?> queryParams = Map.of("withCounts", true, limitKeyword, 1);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/101/commits", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestActivities() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-activities.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final ActivitiesPage activities = api.listActivities(projectKey, repoKey, 1, 5, 0).getBody();
            assertThat(activities).isNotNull();

            Assertions.assertThat(activities.getValues()).hasSize(3);
            assertThat(activities.getValues().get(1).getId() == 29733L).isTrue();
            Activities foundActivities = null;
            for (final Activities act : activities.getValues()) {
                if (act.getCommentAction() != null && act.getCommentAction().equals("ADDED") && act.getComment() != null) {
                    foundActivities = act;
                }
            }
            assertThat(foundActivities).isNotNull();
            final Comments comments = foundActivities.getComment();
            assertThat(comments.getPermittedOperations()).isNotNull();
            assertThat(comments.getPermittedOperations().isDeletable()).isTrue();
            assertThat(comments.getPermittedOperations().isTransitionable()).isFalse();
            assertThat(comments.getTasks().size()).isEqualTo(1);
            final Task task = comments.getTasks().get(0);
            assertThat(task.getAnchor().getType()).isEqualTo("COMMENT");
            assertThat(task.getState()).isEqualTo("OPEN");
            assertThat(task.getAnchor().getProperties().containsKey("likedBy"));
            assertThat(task.getAnchor().getProperties().containsKey("repositoryId"));

            final Map<String, ?> queryParams = Map.of(startKeyword, "0", limitKeyword, 5);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/1/activities", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestActivitiesOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-activities-error.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            try {
                final ActivitiesPage activities = api.listActivities(projectKey, repoKey, 1, 5, 0).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            final Map<String, ?> queryParams = Map.of(startKeyword, "0", limitKeyword, 5);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/1/activities", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestParticipants() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-participants.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final ParticipantsPage participants = api.listParticipants(projectKey, repoKey, 1, 5, 0).getBody();
            assertThat(participants).isNotNull();

            Assertions.assertThat(participants.getValues()).hasSize(1);
            Assertions.assertThat(participants.getValues().get(0).isApproved()).isFalse();

            final Map<String, ?> queryParams = Map.of(startKeyword, "0", limitKeyword, 5);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/1/participants", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetPullRequestParticipantsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-participants-error.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            try {
                final ParticipantsPage participants = api.listParticipants(projectKey, repoKey, 1, 5, 0).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            final Map<String, ?> queryParams = Map.of(startKeyword, "0", limitKeyword, 5);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/projects/PRJ/repos/my-repo/pull-requests/1/participants", queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testPullRequestAssignPaticipants() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/participants.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final Long pullRequestId = 839L;
            final User user = new User(bobKeyword, BOB_EMAIL_ADDRESS, 123, bobKeyword, true, bobKeyword, USER_TYPE);
            final CreateParticipants participants = new CreateParticipants(user, null, Participants.Role.REVIEWER,
                    false, Participants.Status.UNAPPROVED);
            final Participants success = api.assignParticipant(projectKey, repoKey, pullRequestId, participants).getBody();
            assertThat(success).isNotNull();

            assertSent(server, postMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId + "/participants");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testPullRequestAssignPaticipantsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/participants-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final Long pullRequestId = 839L;
            final User user = new User(bobKeyword, BOB_EMAIL_ADDRESS, 123, bobKeyword, true, bobKeyword, USER_TYPE);
            final CreateParticipants participants = new CreateParticipants(user, null, Participants.Role.REVIEWER,
                    false, Participants.Status.UNAPPROVED);
            try {
                final Participants success = api.assignParticipant(projectKey, repoKey, pullRequestId, participants).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, postMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId + "/participants");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePullRequestPaticipants() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final Long pullRequestId = 839L;
            final String userSlug = "bbdfgf";
            final ResponseEntity<Void> success = api.deleteParticipant(projectKey, repoKey, pullRequestId, userSlug);
            assertThat(success).isNotNull();

            assertSent(server, "DELETE", restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId + PARTICIPANTS_PATH + userSlug);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePullRequestPaticipantsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {

            final Long pullRequestId = 839L;
            final String userSlug = "bbdfgf";
            try {
                final ResponseEntity<Void> success = api.deleteParticipant(projectKey, repoKey, pullRequestId, userSlug);
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, "DELETE", restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId + PARTICIPANTS_PATH + userSlug);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testAddPullRequestParticipants() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/participants.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(201));

        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final Long pullRequestId = 839L;
            final User user = new User(bobKeyword, BOB_EMAIL_ADDRESS, 123, bobKeyword, true, bobKeyword, USER_TYPE);
            final CreateParticipants participants = new CreateParticipants(user, null, Participants.Role.REVIEWER,
                    false, Participants.Status.APPROVED);
            final Participants resultParticipants = api.addParticipant(projectKey, repoKey, pullRequestId, bobKeyword, participants).getBody();
            assertThat(resultParticipants).isNotNull();

            assertSent(server, "PUT", restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId + PARTICIPANTS_PATH + bobKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testAddPullRequestParticipantsOnError() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(401));

        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final Long pullRequestId = 839L;
            final User user = new User(bobKeyword, BOB_EMAIL_ADDRESS, 123, bobKeyword, true, bobKeyword, USER_TYPE);
            final CreateParticipants participants = new CreateParticipants(user, null, Participants.Role.REVIEWER,
                    false, Participants.Status.APPROVED);
            try {
                final Participants resultParticipants = api.addParticipant(projectKey, repoKey, pullRequestId, bobKeyword, participants).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, "PUT", restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId + PARTICIPANTS_PATH + bobKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteMergedPullRequest() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-delete-merged.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(409));

        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final Long pullRequestId = 839L;
            final Long prVersion = 1L;
            try {
                final ResponseEntity<Void> success = api.delete(projectKey, repoKey, pullRequestId, new PullRequestVersion(Math.toIntExact(prVersion)));
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteBadVersionOfPullRequest() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-delete-invalid-version.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(409));

        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final Long pullRequestId = 839L;
            final Long badPrVersion = 1L;
            try {
                final ResponseEntity<Void> success = api.delete(projectKey, repoKey, pullRequestId, new PullRequestVersion(Math.toIntExact(badPrVersion)));
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteNonExistentPullRequest() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-delete-non-existent.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));

        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final Long pullRequestId = 999L;
            final Long prVersion = 1L;
            try {
                final ResponseEntity<Void> success = api.delete(projectKey, repoKey, pullRequestId, new PullRequestVersion(Math.toIntExact(prVersion)));
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }
            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePullRequest() throws Exception {
        final MockWebServer server = mockWebServer();
//        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/pull-request-delete.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .setResponseCode(204))
        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));

        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final PullRequestApi api = baseApi.pullRequestApi();
        try {
            final Long pullRequestId = 999L;
            final Long prVersion = 1L;
            final ResponseEntity<Void> success = api.delete(projectKey, repoKey, pullRequestId, new PullRequestVersion(Math.toIntExact(prVersion)));
            assertThat(success).isNotNull();

            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + pullRequestsPath
                    + pullRequestId);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

}
