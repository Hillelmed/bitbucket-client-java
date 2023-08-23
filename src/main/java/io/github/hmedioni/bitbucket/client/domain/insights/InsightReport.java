package io.github.hmedioni.bitbucket.client.domain.insights;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsightReport {


    public long createdDate;
    @Nullable
    public String details;
    @Nullable
    public String key;
    @Nullable
    public String link;
    @Nullable
    public String logoUrl;
    @Nullable
    public ReportResult result;
    @Nullable
    public String title;
    @Nullable
    public String reporter;
    @Nullable
    public List<InsightReportData> data;

    public enum ReportResult {
        PASS,
        FAIL
    }

}
