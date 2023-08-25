package io.github.hmedioni.bitbucket.client.domain.insights;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


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

    public enum ReportResult {
        PASS,
        FAIL
    }

}
