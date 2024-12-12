package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.insights.Annotation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Create annotations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnnotations {

    private List<Annotation> annotations;

}
