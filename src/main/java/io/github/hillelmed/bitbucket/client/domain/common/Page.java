package io.github.hillelmed.bitbucket.client.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Page.
 *
 * @param <T> the type parameter
 */
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
