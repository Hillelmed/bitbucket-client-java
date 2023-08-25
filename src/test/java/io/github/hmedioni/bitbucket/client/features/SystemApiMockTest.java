package io.github.hmedioni.bitbucket.client.features;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.domain.system.*;
import io.github.hmedioni.bitbucket.client.features.blocking.*;
import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Mock tests for the {@link SystemApi} class.
 */
@Test(groups = "unit", testName = "SystemApiMockTest")
public class SystemApiMockTest extends BaseBitbucketMockTest {

    private final String versionRegex = "^\\d+\\.\\d+\\.\\d+$";

    public void testGetVersion() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/version.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final Version version = baseApi.systemApi().version().getBody();
            assertThat(version).isNotNull();
            assertThat(version.getId()).matches(versionRegex);
            assertSent(server, "GET", "/rest/api/" + "latest" + "/application-properties");
        } finally {
            server.shutdown();
        }
    }
}
