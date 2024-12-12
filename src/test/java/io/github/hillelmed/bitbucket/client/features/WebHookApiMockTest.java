package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.repository.*;
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
 * Mock tests for the {@link WebHookApi} class.
 */
@Test(groups = "unit", testName = "WebHookApiMockTest")
public class WebHookApiMockTest extends BaseBitbucketMockTest {

    private final String projectKey = "PRJ";
    private final String repoKey = "my-repo";
    private final String postMethod = "POST";
    private final String getMethod = "GET";
    private final String deleteMethod = "DELETE";
    private final String putMethod = "PUT";

    private final String reposPath = "/repos/";
    private final String restApiPath = "/rest/api/";
    private final String projectsPath = "/projects/";
    private final String webHooksEndpoint = "/webhooks";

    private final String webHooksApiPath = restApiPath + "latest"
            + projectsPath + projectKey + reposPath + repoKey + webHooksEndpoint;

    private final String webHookName = "Webhook Name";
    private final String webHookKey = "10";
    private final String webHookUrl = "http://example.com";
    private final WebHookConfiguration webHookConfiguration = new WebHookConfiguration("password");

    private final String webHookErrorJsonFile = "/mocks/repository-webhook-errors.json";
    private final String webHookJsonFile = "/mocks/repository-webhook.json";
    private final String webHookPageJsonFile = "/mocks/repository-webhook-page.json";
    private final String limitKeyword = "limit";
    private final String startKeyword = "start";


    public void testCreateWebHook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(webHookJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();
        final List<WebHook.EventType> events = new ArrayList<WebHook.EventType>();
        events.add(WebHook.EventType.REPO_CHANGED);
        events.add(WebHook.EventType.REPO_MODIFIED);
        try {
            final CreateWebHook createWebHook = CreateWebHook.create(
                    webHookName, events, webHookUrl, true, webHookConfiguration);
            final WebHook webHook = api.create(projectKey, repoKey, createWebHook).getBody();
            assertThat(webHook).isNotNull();

            assertThat(webHook.getEvents().equals(events)).isTrue();
            assertSent(server, postMethod, webHooksApiPath);
        } finally {
            baseApi.close();
            server.shutdown();
        }

    }

    public void testCreateWebHookOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(webHookErrorJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();
        final List<WebHook.EventType> events = new ArrayList<WebHook.EventType>();
        try {
            final CreateWebHook createWebHook = CreateWebHook.create(
                    webHookName, events, webHookUrl, true, webHookConfiguration);
            try {
                final WebHook webHook = api.create(projectKey, repoKey, createWebHook).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, "POST", webHooksApiPath);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetWebHook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(webHookJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();
        try {
            final WebHook webHook = api.get(projectKey, repoKey, webHookKey).getBody();

            assertThat(webHook).isNotNull();

            assertSent(server, getMethod, webHooksApiPath + "/" + webHookKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetWebHookOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(webHookErrorJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();
        try {
            try {
                final WebHook webHook = api.get(projectKey, repoKey, webHookKey).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, getMethod, webHooksApiPath + "/" + webHookKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListWebHooks() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(webHookPageJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();

        try {
            final WebHookPage webHookPage = api.list(projectKey, repoKey, 0, 100).getBody();
            assertThat(webHookPage).isNotNull();
            Assertions.assertThat(webHookPage.getValues()).isNotEmpty();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, webHooksApiPath, queryParams);
            assertThat(webHookPage.getValues().size() > 0).isEqualTo(true);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListWebHooksOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(webHookErrorJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();

        try {
            try {
                final WebHookPage webHookPage = api.list(projectKey, repoKey, 0, 100).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server, getMethod, webHooksApiPath, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testUpdateWebHook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(webHookJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();
        final List<WebHook.EventType> events = new ArrayList<WebHook.EventType>();

        try {
            events.add(WebHook.EventType.REPO_CHANGED);
            events.add(WebHook.EventType.REPO_MODIFIED);
            final CreateWebHook createWebHook = CreateWebHook.create(
                    webHookName, events, webHookUrl, true, webHookConfiguration);
            final WebHook webHook = api.update(projectKey, repoKey, webHookKey, createWebHook).getBody();
            assertThat(webHook.getEvents()).isEqualTo(events);
            assertThat(webHook).isNotNull();

            assertSent(server, putMethod, webHooksApiPath + "/" + webHookKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testUpdateWebHookOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(webHookErrorJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();
        final List<WebHook.EventType> events = new ArrayList<WebHook.EventType>();

        try {
            final CreateWebHook createWebHook = CreateWebHook.create(
                    webHookName, events, webHookUrl, true, webHookConfiguration);
            try {
                final WebHook webHook = api.update(projectKey, repoKey, webHookKey, createWebHook).getBody();
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, putMethod, webHooksApiPath + "/" + webHookKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }

    }

    public void testDeleteWebHook() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();
        try {
            final ResponseEntity<Void> ref = api.delete(projectKey, repoKey, webHookKey);
            assertThat(ref).isNotNull();

            assertSent(server, deleteMethod, webHooksApiPath + "/" + webHookKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteWebHookOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final WebHookApi api = baseApi.webHookApi();
        try {
            try {
                final ResponseEntity<Void> ref = api.delete(projectKey, repoKey, webHookKey);
            } catch (BitbucketAppException e) {
                assertThat(e).isNotNull();
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server, deleteMethod, webHooksApiPath + "/" + webHookKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }
}
