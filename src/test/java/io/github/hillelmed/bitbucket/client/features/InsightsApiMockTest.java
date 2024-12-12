package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.insights.*;
import io.github.hillelmed.bitbucket.client.exception.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import io.github.hillelmed.bitbucket.client.options.*;
import okhttp3.mockwebserver.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "unit", testName = "InsightApiMockTest")
public class InsightsApiMockTest extends BaseBitbucketMockTest {
    private final String projectKey = "PRJ";
    private final String repoKey = "my-repo";
    private final String commitHash = "abcdef0123abcdef4567abcdef8987abcdef6543";

    private final String getMethod = "GET";
    private final String deleteMethod = "DELETE";
    private final String putMethod = "PUT";

    private final String restApiPath = "/rest/insights/";

    private final String projectsKeyword = "/projects/";
    private final String repoKeyword = "/repos/";
    private final String commitKeyword = "/commits/";
    private final String annotationsKeyword = "/annotations";
    private final String reportsKeyword = "/reports";


    private final String qwertyKeyword = "qwerty";
    private final String limitKeyword = "limit";
    private final String startKeyword = "start";
    private final String annotationsJsonFile = "/mocks/annotations.json";
    private final String mockPath = ".gitignore";

    public void testListReports() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/insight-report-page.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {

            final InsightReportPage insightReportPage = api.listReports(projectKey, repoKey, commitHash, 100, 0).getBody();
            assertThat(insightReportPage).isNotNull();
            Assertions.assertThat(insightReportPage.getValues()).isNotEmpty();

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);

            assertSent(server,
                    getMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey + repoKeyword + repoKey + commitKeyword + commitHash + reportsKeyword, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListReportsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {
            try {
                final InsightReportPage insightReportPage = api.listReports(projectKey, repoKey, commitHash, 100, 0).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            final Map<String, ?> queryParams = Map.of(limitKeyword, 100, startKeyword, 0);
            assertSent(server,
                    getMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey + repoKeyword + repoKey + commitKeyword + commitHash + reportsKeyword, queryParams);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetReport() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/insight-report.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {

            final String reportKey = qwertyKeyword;
            final InsightReport report = api.getReport(projectKey, repoKey, commitHash, reportKey).getBody();
            assertThat(report).isNotNull();
            assertThat(report.getKey()).isEqualTo(reportKey);

            assertSent(server,
                    getMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey + repoKeyword + repoKey + commitKeyword + commitHash + reportsKeyword + "/" + reportKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetReportOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/insight-report-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {

            final String reportKey = qwertyKeyword;
            try {
                final InsightReport report = api.getReport(projectKey, repoKey, commitHash, reportKey).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server,
                    getMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey + repoKeyword + repoKey + commitKeyword + commitHash + reportsKeyword + "/" + reportKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateReport() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/insight-report.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {

            final String reportKey = qwertyKeyword;
            final InsightReportData reportData = InsightReportData.createPercentage("Code Coverage", (byte) 15);
            final CreateInsightReport createInsightReport = new CreateInsightReport("details",
                    "http://example.com",
                    "http://example.com/logourl",
                    CreateInsightReport.RESULT.PASS,
                    "reportTitle",
                    "Bitbucket-rest",
                    Collections.singletonList(reportData));
            final InsightReport report = api.createReport(projectKey, repoKey, commitHash, reportKey, createInsightReport).getBody();
            assertThat(report).isNotNull();
            assertThat(report.getKey()).isEqualTo(reportKey);

            assertSent(server,
                    putMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey + repoKeyword + repoKey + commitKeyword + commitHash + reportsKeyword + "/" + reportKey);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteReport() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final String reportKey = qwertyKeyword;
            final ResponseEntity<Void> success = baseApi.insightsApi().deleteReport(projectKey, repoKey, commitHash, reportKey);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server,
                    deleteMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey + repoKeyword + repoKey + commitKeyword + commitHash + reportsKeyword + "/" + reportKey);
        } finally {
            server.shutdown();
        }
    }

    public void testGetAnnotations() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(annotationsJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {

            final String reportKey = qwertyKeyword;
            final AnnotationsResponse annotations = api.getAnnotationsByReport(projectKey, repoKey, commitHash, reportKey).getBody();
            assertThat(annotations).isNotNull();
            assertThat(annotations.getTotalCount()).isEqualTo(3);

            assertSent(server,
                    getMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey
                            + repoKeyword + repoKey
                            + commitKeyword + commitHash
                            + reportsKeyword + "/" + reportKey
                            + annotationsKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testGetAnnotationsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {

            final String reportKey = qwertyKeyword;
            try {
                final AnnotationsResponse annotationsResponse = api.getAnnotationsByReport(projectKey, repoKey, commitHash, reportKey).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            assertSent(server,
                    getMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey
                            + repoKeyword + repoKey
                            + commitKeyword + commitHash
                            + reportsKeyword + "/" + reportKey
                            + annotationsKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListAnnotations() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(annotationsJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {
            final AnnotationsResponse annotations = api.listAnnotations(projectKey, repoKey, commitHash, null, null, null, null).getBody();
            assertThat(annotations).isNotNull();
            assertThat(annotations.getTotalCount()).isEqualTo(3);

            assertSent(server,
                    getMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey
                            + repoKeyword + repoKey
                            + commitKeyword + commitHash
                            + annotationsKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testListAnnotationsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/repository-not-exist.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {
            try {
                final AnnotationsResponse annotations = api.listAnnotations(projectKey, repoKey, commitHash, null, null, null, null).getBody();
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            assertSent(server,
                    getMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey
                            + repoKeyword + repoKey
                            + commitKeyword + commitHash
                            + annotationsKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateAnnotation() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(annotationsJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {
            final String reportKey = qwertyKeyword;
            final Annotation annotation = new Annotation(
                    null,
                    3,
                    "",
                    "",
                    mockPath,
                    Annotation.AnnotationSeverity.LOW,
                    Annotation.AnnotationType.BUG);
            final ResponseEntity<Void> requestStatus = api.createAnnotation(projectKey, repoKey, commitHash, reportKey, qwertyKeyword, annotation);
            assertThat(requestStatus).isNotNull();
            assertThat(requestStatus.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server,
                    putMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey
                            + repoKeyword + repoKey
                            + commitKeyword + commitHash
                            + reportsKeyword + "/" + reportKey
                            + annotationsKeyword + "/" + qwertyKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateAnnotationOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/insight-report-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {
            final String reportKey = qwertyKeyword;
            final Annotation annotation = new Annotation(
                    null,
                    3,
                    "",
                    "",
                    mockPath,
                    Annotation.AnnotationSeverity.LOW,
                    Annotation.AnnotationType.BUG);
            try {
                final ResponseEntity<Void> requestStatus = api.createAnnotation(projectKey, repoKey, commitHash, reportKey, qwertyKeyword, annotation);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }


            assertSent(server,
                    putMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey
                            + repoKeyword + repoKey
                            + commitKeyword + commitHash
                            + reportsKeyword + "/" + reportKey
                            + annotationsKeyword + "/" + qwertyKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateAnnotations() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource(annotationsJsonFile)).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {
            final String reportKey = qwertyKeyword;
            final Annotation annotation = new Annotation(
                    null,
                    3,
                    "",
                    "",
                    mockPath,
                    Annotation.AnnotationSeverity.LOW,
                    Annotation.AnnotationType.BUG);
            final CreateAnnotations createAnnotations = new CreateAnnotations(Collections.singletonList(annotation));
            final ResponseEntity<Void> requestStatus = api.createAnnotations(projectKey, repoKey, commitHash, reportKey, createAnnotations);
            assertThat(requestStatus).isNotNull();
            assertThat(requestStatus.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server,
                    "POST",
                    restApiPath + "latest"
                            + projectsKeyword + projectKey
                            + repoKeyword + repoKey
                            + commitKeyword + commitHash
                            + reportsKeyword + "/" + reportKey
                            + annotationsKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testCreateAnnotationsOnError() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/mocks/insight-report-error.json")).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404));
        final BitbucketApi baseApi = api("http://localhost:" + server.getPort());
        final InsightsApi api = baseApi.insightsApi();
        try {
            final String reportKey = qwertyKeyword;
            final Annotation annotation = new Annotation(
                    null,
                    3,
                    "",
                    "",
                    mockPath,
                    Annotation.AnnotationSeverity.LOW,
                    Annotation.AnnotationType.BUG);
            final CreateAnnotations createAnnotations = new CreateAnnotations(Collections.singletonList(annotation));
            try {
                final ResponseEntity<Void> requestStatus = api.createAnnotations(projectKey, repoKey, commitHash, reportKey, createAnnotations);
            } catch (BitbucketAppException e) {
                Assertions.assertThat(e.errors()).isNotEmpty();
            }

            assertSent(server,
                    "POST",
                    restApiPath + "latest"
                            + projectsKeyword + projectKey
                            + repoKeyword + repoKey
                            + commitKeyword + commitHash
                            + reportsKeyword + "/" + reportKey
                            + annotationsKeyword);
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeleteAnnotation() throws Exception {
        final MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(204));
        try (final BitbucketApi baseApi = api("http://localhost:" + server.getPort())) {

            final String reportKey = qwertyKeyword;
            final ResponseEntity<Void> success = baseApi.insightsApi().deleteAnnotation(projectKey, repoKey, commitHash, reportKey, null);
            assertThat(success).isNotNull();
            assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();

            assertSent(server,
                    deleteMethod,
                    restApiPath + "latest"
                            + projectsKeyword + projectKey
                            + repoKeyword + repoKey
                            + commitKeyword + commitHash
                            + reportsKeyword + "/" + reportKey
                            + annotationsKeyword);
        } finally {
            server.shutdown();
        }
    }
}
