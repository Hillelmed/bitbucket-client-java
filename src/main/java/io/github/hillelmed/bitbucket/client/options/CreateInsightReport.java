package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.insights.InsightReportData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * The type Create insight report.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInsightReport {
    @Nullable
    private String details;
    @Nullable
    private String link;
    @Nullable
    private String logoUrl;
    @Nullable
    private CreateInsightReport.RESULT result;
    private String title;
    @Nullable
    private String reporter;
    private List<InsightReportData> data;

    /**
     * The enum Result.
     */
    public enum RESULT {
        /**
         * Pass result.
         */
        PASS,
        /**
         * Fail result.
         */
        FAIL
    }

}
