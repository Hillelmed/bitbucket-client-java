package io.github.hmedioni.bitbucket.client.domain.insights;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationsResponse {
    private int totalCount;

    @Nullable
    private List<Annotation> annotations;

}
