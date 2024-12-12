package io.github.hillelmed.bitbucket.client.features;

import io.github.hillelmed.bitbucket.client.*;
import io.github.hillelmed.bitbucket.client.domain.branch.*;
import io.github.hillelmed.bitbucket.client.domain.insights.*;
import io.github.hillelmed.bitbucket.client.exception.*;
import io.github.hillelmed.bitbucket.client.features.blocking.*;
import io.github.hillelmed.bitbucket.client.options.*;
import org.assertj.core.api.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Test(groups = "live", testName = "InsightsApiLiveTest")
public class InsightsApiLiveTest extends BaseBitbucketApiLiveTest {

    private final String reportKey = TestUtilities.randomStringLettersOnly().toLowerCase(Locale.ROOT);
    private GeneratedTestContents generatedTestContents;
    private String projectKey;
    private String repoKey;
    private String commitHash;
    private CreateInsightReport createInsightReport;
    private CreateInsightReport createInsightReport2;
    private CreateAnnotations createAnnotations;
    private Annotation annotation;
    private String annotationId;

    @BeforeClass
    public void init() {
        generatedTestContents = TestUtilities.initGeneratedTestContents(
                this.bitbucketClient, this.bitbucketProperties);
        this.projectKey = generatedTestContents.project.getKey();
        this.repoKey = generatedTestContents.repository.getName();
        final Branch branch = bitbucketClient.api().branchApi().getDefault(projectKey, repoKey).getBody();
        assertThat(branch).isNotNull();

        commitHash = branch.getLatestCommit();

        final String linkFormat = "https://%s";
        final List<InsightReportData> data = Arrays.asList(
                InsightReportData.createBoolean(TestUtilities.randomStringLettersOnly(),
                        true),
                InsightReportData.createDate(TestUtilities.randomStringLettersOnly(),
                        LocalDate.of(2019, 12, 18)),
                InsightReportData.createDuration(TestUtilities.randomStringLettersOnly(),
                        Duration.ofSeconds(25)),
                InsightReportData.createLink(TestUtilities.randomStringLettersOnly(),
                        String.format(linkFormat, TestUtilities.randomString()),
                        TestUtilities.randomString())
        );
        createInsightReport = new CreateInsightReport(TestUtilities.randomString(),
                String.format(linkFormat, TestUtilities.randomString()),
                String.format(linkFormat, TestUtilities.randomString()),
                CreateInsightReport.RESULT.PASS,
                TestUtilities.randomStringLettersOnly(),
                TestUtilities.randomString(),
                data);

        final List<InsightReportData> data2 = Arrays.asList(
                InsightReportData.createLink(TestUtilities.randomStringLettersOnly(),
                        String.format(linkFormat, TestUtilities.randomString()),
                        TestUtilities.randomString()),
                InsightReportData.createNumber(TestUtilities.randomStringLettersOnly(),
                        54321),
                InsightReportData.createPercentage(TestUtilities.randomStringLettersOnly(),
                        (byte) 42),
                InsightReportData.createText(TestUtilities.randomStringLettersOnly(),
                        TestUtilities.randomString())
        );
        createInsightReport2 = new CreateInsightReport(TestUtilities.randomString(),
                String.format(linkFormat, TestUtilities.randomString()),
                String.format(linkFormat, TestUtilities.randomString()),
                CreateInsightReport.RESULT.PASS,
                TestUtilities.randomStringLettersOnly(),
                TestUtilities.randomString(),
                data2);

        annotationId = TestUtilities.randomStringLettersOnly();
        createAnnotations = new CreateAnnotations(Collections.singletonList(
                new Annotation(
                        annotationId,
                        0,
                        String.format(linkFormat, TestUtilities.randomString()),
                        TestUtilities.randomStringLettersOnly(),
                        TestUtilities.randomStringLettersOnly(),
                        Annotation.AnnotationSeverity.LOW,
                        Annotation.AnnotationType.BUG
                )
        ));

        annotation = new Annotation(
                TestUtilities.randomStringLettersOnly(),
                0,
                String.format(linkFormat, TestUtilities.randomString()),
                TestUtilities.randomStringLettersOnly(),
                TestUtilities.randomStringLettersOnly(),
                Annotation.AnnotationSeverity.LOW,
                Annotation.AnnotationType.BUG
        );
    }

    @Test
    public void testCreateReport() {
        final InsightReport report = api().createReport(projectKey, repoKey, commitHash, reportKey, createInsightReport).getBody();
        assertThat(report).isNotNull();
        assertThat(reportKey).isEqualToIgnoringCase(report.getKey());

        final InsightReport report2 = api().createReport(projectKey, repoKey, commitHash, reportKey, createInsightReport2).getBody();
        assertThat(report2).isNotNull();

    }

    @Test(dependsOnMethods = "testCreateReport") //NOPMD
    public void testGetReport() {
        final InsightReport report = api().getReport(projectKey, repoKey, commitHash, reportKey).getBody();
        assertThat(report).isNotNull();
        assertThat(reportKey.equalsIgnoreCase(report.getKey())).isTrue();
    }

    @Test(dependsOnMethods = "testCreateReport")
    public void testListReport() {
        final InsightReportPage page = api().listReports(projectKey, repoKey, commitHash, 100, 0).getBody();
        assertThat(page).isNotNull();
        assertThat(page.getSize()).isPositive();
        final List<InsightReport> insightReports = page.getValues();
        assertThat(insightReports).isNotEmpty();
        assertThat(insightReports.stream().anyMatch(r -> r.getKey().equalsIgnoreCase(reportKey))).isTrue();
    }

    @Test
    public void testDeleteReportNonExistent() {
        try {
            final ResponseEntity<Void> success = api().deleteReport(projectKey,
                    repoKey,
                    TestUtilities.randomStringLettersOnly(),
                    TestUtilities.randomStringLettersOnly());
        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }

    }

    @Test
    public void testGetReportNonExistent() {
        try {
            final InsightReport report = api().getReport(projectKey, repoKey, commitHash, TestUtilities.randomStringLettersOnly()).getBody();
        } catch (BitbucketAppException e) {
            assertThat(e.errors().isEmpty()).isFalse();
        }
    }

    @Test(dependsOnMethods = "testCreateReport")
    public void testCreateAnnotations() {
        final ResponseEntity<Void> success = api().createAnnotations(projectKey, repoKey, commitHash, reportKey, createAnnotations);
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test(dependsOnMethods = "testCreateReport")
    public void testCreateAnnotation() {
        String externalId = TestUtilities.randomStringLettersOnly();
        annotation.setExternalId(null);
        final ResponseEntity<Void> success = api().createAnnotation(projectKey,
                repoKey,
                commitHash,
                reportKey,
                externalId,
                annotation);
        assertThat(success.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test(dependsOnMethods = "testCreateAnnotations")
    public void testGetAnnotationsByReport() {
        final AnnotationsResponse annotations = api().getAnnotationsByReport(projectKey, repoKey, commitHash, reportKey).getBody();
        assertThat(annotations).isNotNull();
        assertThat(annotations.getTotalCount()).isPositive();
        assertThat(annotations.getAnnotations().stream().anyMatch(a -> a.getExternalId().equalsIgnoreCase(annotationId))).isTrue();
    }

    @Test(dependsOnMethods = "testCreateAnnotations")
    public void testListAnnotation() {
        final AnnotationsResponse annotations = api().listAnnotations(projectKey,
                repoKey,
                commitHash,
                annotationId,
                null,
                null,
                null).getBody();
        assertThat(annotations).isNotNull();
        assertThat(annotations.getTotalCount()).isPositive();
        assertThat(annotations.getAnnotations().stream().anyMatch(a -> a.getExternalId().equals(annotationId))).isTrue();
    }

    @Test
    public void testDeleteAnnotationNonExistent() {
        try {
            final ResponseEntity<Void> success = api().deleteAnnotation(projectKey,
                    TestUtilities.randomStringLettersOnly(),
                    commitHash,
                    TestUtilities.randomStringLettersOnly(),
                    TestUtilities.randomStringLettersOnly());

        } catch (BitbucketAppException e) {
            Assertions.assertThat(e.errors()).isNotEmpty();
        }
    }

    @AfterClass
    public void fin() {
        TestUtilities.terminateGeneratedTestContents(bitbucketClient.api(), generatedTestContents);
    }

    private InsightsApi api() {
        return bitbucketClient.api().insightsApi();
    }
}
