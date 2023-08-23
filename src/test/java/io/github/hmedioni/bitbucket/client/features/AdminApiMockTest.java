package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.admin.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "unit", testName = "AdminApiMockTest")
public class AdminApiMockTest extends BaseBitbucketMockTest {

    private static final String USERS_POSTFIX = "/admin/users";
    private static final String USER_TEXT = "user";
    private static final String NOTIFY_TEXT = "notify";
    private static final String DELETE_METHOD = "DELETE";

    private final String limitKeyword = "limit";
    private final String startKeyword = "start";
    private final String restApiPath = "/rest/api/";
    private final String getMethod = "GET";

    private final String localContext = "test";

    private static Map<String, String> createUserQueryParams() {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", USER_TEXT);
        queryParams.put("password", "pass");
        queryParams.put("displayName", "display");
        queryParams.put("emailAddress", "email");
        queryParams.put("addToDefaultGroup", "false");
        queryParams.put(NOTIFY_TEXT, NOTIFY_TEXT);
        return queryParams;
    }

    public void testGetListUserByGroup() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/admin-list-user-by-group.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final UserPage up = baseApi.adminApi().listUsersByGroup(localContext, null, 0, 2).getBody();
            assertThat(up).isNotNull();
            assertThat(up.getSize() == 2).isTrue();
            assert up.getValues().get(0).getSlug() != null;
            Assertions.assertThat(up.getValues().get(0).getSlug().equals("bob123")).isTrue();

            final Map<String, ?> queryParams = Map.of("context", localContext, limitKeyword, 2, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/admin/groups/more-members", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testGetListUserByGroupOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
                .setBody(payloadFromResource("/mocks/admin-list-user-by-group-error.json"))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(401));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            try {
                baseApi.adminApi().listUsersByGroup(localContext, null, 0, 2);
            } catch (BitbucketAppException bitbucketAppException) {
                assertThat(bitbucketAppException).isNotNull();
                Assertions.assertThat(bitbucketAppException.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of("context", localContext, limitKeyword, 2, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + "/admin/groups/more-members", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testListUsers() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/admin-list-users.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final AdminApi api = baseApi.adminApi();
        try {
            final UserPage up = api.listUsers("jcitizen", 0, 2).getBody();
            assertThat(up).isNotNull();
            assertThat(up.getSize() == 1).isTrue();
            Assertions.assertThat(up.getValues().get(0).getSlug().equals("jcitizen")).isTrue();

            final Map<String, ?> queryParams = Map.of("filter", "jcitizen", limitKeyword, 2, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + USERS_POSTFIX, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListUsersOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/admin-list-user-by-group-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(401));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        try (baseApi) {
            final AdminApi api = baseApi.adminApi();
            api.listUsers("blah blah", 0, 2);
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
            final Map<String, ?> queryParams = Map.of("filter", "blah%20blah", limitKeyword, 2, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + USERS_POSTFIX, queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testCreateUser() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final AdminApi api = baseApi.adminApi();
        try {
            final ResponseEntity<Void> status = api.createUser(USER_TEXT, "pass", "display", "email", false, NOTIFY_TEXT);
            assertThat(status.getStatusCode().value()).isEqualTo(204);

            final Map<String, String> queryParams = createUserQueryParams();
            assertSent(server, "POST", restApiPath + "latest" + USERS_POSTFIX, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateUserOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        try (baseApi) {
            final AdminApi api = baseApi.adminApi();
            api.createUser(USER_TEXT, "pass", "display", "email", false, NOTIFY_TEXT);
        } catch (Exception e) {
            if (e instanceof BitbucketAppException) {
                Assertions.assertThat(((BitbucketAppException) e).errors().get(0).isConflicted()).isFalse();
                final Map<String, String> queryParams = createUserQueryParams();
                assertSent(server, "POST", restApiPath + "latest" + USERS_POSTFIX, queryParams);
            } else {
                fail("Send other exception");
            }

        } finally {
            server.shutdown();
        }
    }

    public void testDeleteUser() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/admin-delete-user.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final AdminApi api = baseApi.adminApi();
        try {
            final User user = api.deleteUser(USER_TEXT).getBody();
            assertThat(user).isNotNull();

            final Map<String, ?> queryParams = Map.of("name", USER_TEXT);
            assertSent(server, DELETE_METHOD, restApiPath + "latest" + USERS_POSTFIX, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteUserOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/admin-delete-user-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(400));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        try (baseApi) {
            final AdminApi api = baseApi.adminApi();
            final User user = api.deleteUser(USER_TEXT).getBody();
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();

            final Map<String, ?> queryParams = Map.of("name", USER_TEXT);
            assertSent(server, DELETE_METHOD, restApiPath + "latest" + USERS_POSTFIX, queryParams);
        } finally {
            server.shutdown();
        }
    }
}
