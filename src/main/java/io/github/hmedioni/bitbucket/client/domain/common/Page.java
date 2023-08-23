package io.github.hmedioni.bitbucket.client.domain.common;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Page<T> {

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
    @JsonProperty("values")
    private List<T> values;
}
