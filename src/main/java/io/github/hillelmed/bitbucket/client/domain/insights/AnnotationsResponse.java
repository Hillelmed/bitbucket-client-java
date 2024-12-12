package io.github.hillelmed.bitbucket.client.domain.insights;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Annotations response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationsResponse {
    private int totalCount;

    @Nullable
    private List<Annotation> annotations;

}
