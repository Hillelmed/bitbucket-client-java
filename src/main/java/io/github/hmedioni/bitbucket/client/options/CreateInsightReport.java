package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.insights.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;

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

    public enum RESULT {
        PASS,
        FAIL
    }

}
