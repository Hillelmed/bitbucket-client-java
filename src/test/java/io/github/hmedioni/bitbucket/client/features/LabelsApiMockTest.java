package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.labels.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.io.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "unit", testName = "LabelsApiMockTest")
public class LabelsApiMockTest extends BaseBitbucketMockTest {

    public void testListAllLabels() throws IOException {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/labels-list.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(201));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final LabelsPage labelsPage = baseApi.labelsApi().list(null).getBody();
            assertThat(labelsPage).isNotNull();
            assertThat(!labelsPage.getValues().isEmpty()).isTrue();

            assertThat(labelsPage.getValues().get(0).getName()).isEqualTo("labelName");

        } finally {
            server.shutdown();
        }
    }

    public void testListAllLabelError() throws IOException {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/errors.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(401));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final LabelsPage labelsPage = baseApi.labelsApi().list(null).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).hasSizeGreaterThan(0);
            }


        } finally {
            server.shutdown();
        }
    }

    public void testGetLabelByName() throws IOException {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/labels-get-byname.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(201));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            final Label label = baseApi.labelsApi().getLabelByName("label").getBody();
            assertThat(label).isNotNull();
            assertThat(label.getName()).isEqualTo("labelName");

        } finally {
            server.shutdown();
        }
    }

    public void testGetLabelByNameReturnError() throws IOException {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/errors.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(401));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            try {
                final Label label = baseApi.labelsApi().getLabelByName("label").getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).hasSizeGreaterThan(0);

            }

        } finally {
            server.shutdown();
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLabelIfNullThrowException() throws IOException {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/errors.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(401));

        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {
            baseApi.labelsApi().getLabelByName(null);
        } finally {
            server.shutdown();
        }
    }
}
