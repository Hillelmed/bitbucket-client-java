package io.github.hmedioni.bitbucket.client.domain.search;


import lombok.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private Map<String, Object> request;

}
