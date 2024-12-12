package io.github.hillelmed.bitbucket.client.domain.search;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Search path match.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPathMatch {

    private boolean match;

    @Nullable
    private String text;

}
