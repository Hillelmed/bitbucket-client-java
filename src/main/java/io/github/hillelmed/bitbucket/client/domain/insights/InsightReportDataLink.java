package io.github.hillelmed.bitbucket.client.domain.insights;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Insight report data link.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsightReportDataLink {
    @JsonProperty("linktext")
    private String linkText;

    private String href;

}
