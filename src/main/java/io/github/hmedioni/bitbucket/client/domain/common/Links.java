package io.github.hmedioni.bitbucket.client.domain.common;


import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Links {

    private List<Map<String, String>> clone;

    private List<Map<String, String>> self;

}
