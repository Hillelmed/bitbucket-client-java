package io.github.hmedioni.bitbucket.client.domain.search;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPathMatch {

    private boolean match;

    @Nullable
    private String text;

}
