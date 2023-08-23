package io.github.hmedioni.bitbucket.client.domain.insights;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsightReportDataLink {
    @JsonProperty("linktext")
    public String linkText;

    public String href;

}
