package io.github.hillelmed.bitbucket.client.options;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.hillelmed.bitbucket.client.domain.comment.Anchor;
import io.github.hillelmed.bitbucket.client.domain.comment.Parent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Create comment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateComment {

    private String text;

    private String severity;

    @Nullable
    private Parent parent;

    @Nullable
    private Anchor anchor;

}
