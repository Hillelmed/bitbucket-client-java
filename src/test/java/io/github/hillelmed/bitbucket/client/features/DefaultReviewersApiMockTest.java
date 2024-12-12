package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.branch.*;
import io.github.hillelmed.bitbucket.client.domain.defaultreviewers.Condition;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.*;
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
 * Mock tests for the {@link CommitsApi} class.
 */
@Test(groups = "unit", testName = "DefaultReviewersApiMockTest")
public class DefaultReviewersApiMockTest extends BaseBitbucketMockTest {

    private final String projectsPath = "/projects/";
    private final String reposPath = "/repos/";
    private final String defaultReviewersPath = "/rest/default-reviewers/";
    private final String normalKeyword = "NORMAL";
    private final String testEmail = "test@test.com";

    private final String projectKey = "test";
    private final String repoKey = "1234";

    public void testListConditions() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/default-reviwers-list.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final List<Condition> conditions = baseApi.defaultReviewersApi().listConditions(projectKey, repoKey).getBody();
            assertThat(conditions).isNotNull();
            assertThat(conditions.size()).isEqualTo(3);

            assertSent(server, "GET", defaultReviewersPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + "/conditions");
        } finally {
            server.shutdown();
        }
    }

    public void testCreateCondition() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/default-reviwers-create.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Long requiredApprover = 1L;
            final Matcher matcherSrc = new Matcher(Matcher.MatcherId.ANY, true);
            final Matcher matcherDst = new Matcher(Matcher.MatcherId.ANY, true);
            final List<User> listUser = new ArrayList<>();
            listUser.add(new User(projectKey, testEmail, 1, projectKey, true, projectKey, normalKeyword));
            final CreateCondition condition = new CreateCondition(null, matcherSrc, matcherDst, listUser, requiredApprover);

            final Condition returnCondition = baseApi.defaultReviewersApi().createCondition(projectKey, repoKey, condition).getBody();
            assertThat(returnCondition).isNotNull();

            assertThat(returnCondition.getId()).isEqualTo(3L);

            assertSent(server, "POST", defaultReviewersPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + "/condition");
        } finally {
            server.shutdown();
        }
    }

    public void testCreateConditionOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Long requiredApprover = 1L;
            final Matcher matcherSrc = new Matcher(Matcher.MatcherId.ANY, true);
            final Matcher matcherDst = new Matcher(Matcher.MatcherId.ANY, true);
            final List<User> listUser = new ArrayList<>();
            listUser.add(new User(projectKey, testEmail, 1, projectKey, true, projectKey, normalKeyword));
            final CreateCondition condition = new CreateCondition(null, matcherSrc, matcherDst, listUser, requiredApprover);

            try {
                final Condition returnCondition = baseApi.defaultReviewersApi().createCondition(projectKey, "123456", condition).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
                assertThat(e.errors().size()).isEqualTo(1);

            }

            assertSent(server, "POST", defaultReviewersPath + "latest"
                    + projectsPath + projectKey + "/repos/123456/condition");
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateCondition() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/default-reviwers-create.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Long requiredApprover = 1L;
            final Matcher matcherSrc = new Matcher(Matcher.MatcherId.ANY, true);
            final Matcher matcherDst = new Matcher(Matcher.MatcherId.ANY, true);
            final List<User> listUser = new ArrayList<>();
            listUser.add(new User(projectKey, testEmail, 1, projectKey, true, projectKey, normalKeyword));
            final CreateCondition condition = new CreateCondition(10L, matcherSrc, matcherDst, listUser, requiredApprover);

            final Condition returnCondition = baseApi.defaultReviewersApi().updateCondition(projectKey, repoKey, 10L, condition).getBody();
            assertThat(returnCondition).isNotNull();

            assertThat(returnCondition.getId()).isEqualTo(3L);

            assertSent(server, "PUT", defaultReviewersPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + "/condition/10");
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateConditionOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Long requiredApprover = 1L;
            final Matcher matcherSrc = new Matcher(Matcher.MatcherId.ANY, true);
            final Matcher matcherDst = new Matcher(Matcher.MatcherId.ANY, true);
            final List<User> listUser = new ArrayList<>();
            listUser.add(new User(projectKey, testEmail, 1, projectKey, true, projectKey, normalKeyword));
            final CreateCondition condition = new CreateCondition(10L, matcherSrc, matcherDst, listUser, requiredApprover);
            try {
                final Condition returnCondition = baseApi.defaultReviewersApi().updateCondition(projectKey, "123456", 10L, condition).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
                assertThat(e.errors().size()).isEqualTo(1);
            }


            assertSent(server, "PUT", defaultReviewersPath + "latest"
                    + projectsPath + projectKey + "/repos/123456/condition/10");
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteCondition() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final ResponseEntity<Void> success = baseApi.defaultReviewersApi().deleteCondition(projectKey, repoKey, 10L);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
            assertSent(server, "DELETE", defaultReviewersPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + "/condition/10");
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteConditionOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final ResponseEntity<Void> success = baseApi.defaultReviewersApi().deleteCondition(projectKey, repoKey, 10L);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, "DELETE", defaultReviewersPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + "/condition/10");
        } finally {
            server.shutdown();
        }
    }
}
