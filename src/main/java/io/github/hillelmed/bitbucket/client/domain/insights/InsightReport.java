package io.github.hillelmed.bitbucket.client.domain.insights;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Insight report.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsightReport {


    private long createdDate;
    @Nullable
    private String details;
    @Nullable
    private String key;
    @Nullable
    private String link;
    @Nullable
    private String logoUrl;
    @Nullable
    private ReportResult result;
    @Nullable
    private String title;
    @Nullable
    private String reporter;
    @Nullable
    private List<InsightReportData> data;

    /**
     * The enum Report result.
     */
    public enum ReportResult {
        /**
         * Pass report result.
         */
        PASS,
        /**
         * Fail report result.
         */
        FAIL
    }

}
