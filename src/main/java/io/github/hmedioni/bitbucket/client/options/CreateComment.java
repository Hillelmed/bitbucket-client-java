package io.github.hmedioni.bitbucket.client.options;


import com.fasterxml.jackson.annotation.*;
import io.github.hmedioni.bitbucket.client.domain.comment.*;
import lombok.*;
import org.springframework.lang.*;


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
