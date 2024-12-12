package io.github.hillelmed.bitbucket.client.domain.search;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**
 * The type Search request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private Map<String, Object> request;

}
