package io.github.hillelmed.bitbucket.client;

import com.fasterxml.jackson.databind.*;
import io.github.hillelmed.bitbucket.client.config.*;
import lombok.*;
import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.springframework.test.context.testng.*;
import org.testng.annotations.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Base class for all Bitbucket mock tests.
 */
@Test(groups = "unit")
public class BaseBitbucketMockTest extends AbstractTestNGSpringContextTests {

    protected final String restBasePath = "/rest/api/";
    protected final String provider;
    protected final String postMethod = "POST";
    protected final ObjectMapper parser = new ObjectMapper();
    final protected ObjectMapper objectMapper = new ObjectMapper();
    final private String url = "http://127.0.0.1:55555";
    final private String user = "hello";
    final private String password = "world";
    protected BitbucketProperties bitbucketProperties = new BitbucketProperties(url, user, password);
    protected BitbucketClient bitbucketClient = BitbucketClient.create(bitbucketProperties);

    public BaseBitbucketMockTest() {
        provider = "bitbucket";
    }

    /**
     * Create a MockWebServer.
     *
     * @return instance of MockWebServer
     * @throws IOException if unable to start/play server
     */
    public static MockWebServer mockWebServer() throws IOException {
        final MockWebServer server = new MockWebServer();
        server.start();
        return server;
    }

    private static Map<String, String> extractParams(final String path) {

        final int qmIndex = path.indexOf('?');
        if (qmIndex <= 0) {
            return Collections.unmodifiableMap(new HashMap<>());
        }

        final Map<String, String> builder = new HashMap<>();

        final String[] params = path.substring(qmIndex + 1).split("&");
        for (final String param : params) {
            final String[] keyValue = param.split("=", 2);
            if (keyValue.length > 1) {
                builder.put(keyValue[0], keyValue[1]);
            }
        }

        return Collections.unmodifiableMap(builder);
    }

    /**
     * Create API from passed URL.
     *
     * @return instance of BitbucketApi.
     */
    public BitbucketApi api(String url) {
        BitbucketProperties bitbucketProperties1 = new BitbucketProperties(url, user, password);
        return BitbucketClient.create(bitbucketProperties1).api();
    }

    /**
     * Get the String representation of some resource to be used as payload.
     *
     * @param resource String representation of a given resource
     * @return payload in String form
     */
    public String payloadFromResource(final String resource) {
        try {
            return new String((getClass().getResourceAsStream(resource)).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected RecordedRequest assertSent(final MockWebServer server,
                                         final String method,
                                         final String path) throws InterruptedException {

        return assertSent(server, method, path, new HashMap<>());
    }

    protected RecordedRequest assertSent(final MockWebServer server,
                                         final String method,
                                         final String expectedPath,
                                         final Map<String, ?> queryParams) throws InterruptedException {


        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
//        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        final String path = request.getPath();
        final String rawPath = path.contains("?") ? path.substring(0, path.indexOf('?')) : path;
        assertThat(rawPath).isEqualTo(expectedPath);

        final Map<String, String> normalizedParams = new HashMap<>();
        queryParams.forEach((k, v) -> normalizedParams.put(k, v.toString()));
        assertThat(normalizedParams).isEqualTo(extractParams(path));

        return request;
    }

    @SneakyThrows
    protected RecordedRequest assertSent(final MockWebServer server,
                                         final String method,
                                         final String path,
                                         final String json) throws InterruptedException {

        final RecordedRequest request = assertSent(server, method, path);
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(parser.writeValueAsString(json).replace(" ", "")).isEqualTo(parser.writeValueAsString(new String(request.getBody().readByteArray())).replace(" ", ""));
        return request;
    }

    protected RecordedRequest assertSentWithFormData(final MockWebServer server,
                                                     final String method,
                                                     final String path,
                                                     final String body) throws InterruptedException {

        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(new String(request.getBody().readByteArray())).isEqualTo(body);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return request;
    }

    protected RecordedRequest assertSentAcceptText(final MockWebServer server,
                                                   final String method,
                                                   final String path) throws InterruptedException {

        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
        return request;
    }
}
