package io.github.hmedioni.bitbucket.client.domain.common;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PageEmpty {

    @JsonProperty("start")
    private int start;
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("size")
    private int size;
    @JsonProperty("nextPageStart")
    private Integer nextPageStart;
    @JsonProperty("isLastPage")
    private boolean isLastPage;
}
