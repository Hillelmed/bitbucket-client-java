package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.repository.*;
import io.github.hillelmed.bitbucket.client.exception.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Mock tests for the {@link HookApi} class.
 */
@Test(groups = "unit", testName = "HookApiMockTest")
public class HookApiMockTest extends BaseBitbucketMockTest {

    final String testKey = "string-value";
    final String testValue = "this is an arbitrary string";
    private final String projectKey = "PRJ";
    private final String repoKey = "my-repo";
    private final String getMethod = "GET";
    private final String deleteMethod = "DELETE";
    private final String putMethod = "PUT";
    private final String restApiPath = "/rest/api/";
    private final String projectsPath = "/projects/";
    private final String settingsPath = "/settings/";
    private final String hooksPath = settingsPath + "hooks";
    private final String reposPath = "/repos/";
    private final String enabledEndpoint = "/enabled";
    private final String settingsEndpoint = "/settings";
    private final String qwertyKeyword = "qwerty";
    private final String limitKeyword = "limit";
    private final String startKeyword = "start";
    private final String hookErrorJsonFile = "/mocks/repository-hook-error.json";

    public void testListHook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-hooks.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final HookApi api = baseApi.hookApi();
        try {

            final HookPage hookPage = api.list(projectKey, repoKey, 0, 100).getBody();
            assertThat(hookPage).isNotNull();
            Assertions.assertThat(hookPage.getValues()).isNotEmpty();


            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + hooksPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListHookOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final HookApi api = baseApi.hookApi();
        try {

            try {
                final HookPage hookPage = api.list(projectKey, repoKey, 0, 100).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + hooksPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetHook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-hook.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final HookApi api = baseApi.hookApi();
        try {

            final String hookKey = qwertyKeyword;
            final Hook hookPage = api.get(projectKey, repoKey, hookKey).getBody();
            assertThat(hookPage).isNotNull();
            assertThat(hookPage.isEnabled()).isFalse();


            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + hooksPath + "/" + hookKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetHookOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(hookErrorJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final HookApi api = baseApi.hookApi();
        try {

            final String hookKey = qwertyKeyword;
            try {
                final Hook hookPage = api.get(projectKey, repoKey, hookKey).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            assertSent(server, getMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + hooksPath + "/" + hookKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testEnableHook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-hook.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final HookApi api = baseApi.hookApi();
        try {

            final String hookKey = qwertyKeyword;
            final Hook hookPage = api.enable(projectKey, repoKey, hookKey).getBody();
            assertThat(hookPage).isNotNull();
            assertThat(hookPage.isEnabled()).isFalse();


            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + hooksPath + "/" + hookKey + enabledEndpoint);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testEnableHookOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(hookErrorJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final HookApi api = baseApi.hookApi();
        try {

            final String hookKey = qwertyKeyword;
            try {
                final Hook hookPage = api.enable(projectKey, repoKey, hookKey).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, putMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + hooksPath + "/" + hookKey + enabledEndpoint);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDisableHook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-hook.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final HookApi api = baseApi.hookApi();
        try {

            final String hookKey = qwertyKeyword;
            final Hook hookPage = api.disable(projectKey, repoKey, hookKey).getBody();
            assertThat(hookPage).isNotNull();
            assertThat(hookPage.isEnabled()).isFalse();


            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + hooksPath + "/" + hookKey + enabledEndpoint);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDisableHookOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(hookErrorJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final HookApi api = baseApi.hookApi();
        try {

            final String hookKey = qwertyKeyword;
            try {
                final Hook hookPage = api.disable(projectKey, repoKey, hookKey).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, deleteMethod, restApiPath + "latest"
                    + projectsPath + projectKey + reposPath + repoKey + hooksPath + "/" + hookKey + enabledEndpoint);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }


}
